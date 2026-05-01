package com.example.demo.service;

import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.CustomerCreateUpdateDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.Address;
import com.example.demo.entity.City;
import com.example.demo.entity.Country;
import com.example.demo.entity.Customer;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.exception.DuplicateNicException;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final AddressRepository addressRepository;
    
    /**
     * Get customer by ID with all details
     * Prevents N+1 query problem
     */
    public CustomerDTO getCustomerById(Long id) {
        log.info("Fetching customer with ID: {}", id);
        Customer customer = customerRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id, id));
        return convertToDTO(customer);
    }
    
    /**
     * Get customer by NIC
     */
    public CustomerDTO getCustomerByNic(String nic) {
        log.info("Fetching customer with NIC: {}", nic);
        Customer customer = customerRepository.findByNic(nic)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with NIC: " + nic));
        return convertToDTO(customer);
    }
    
    /**
     * Get all customers with addresses
     */
    public List<CustomerDTO> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAllWithAddresses()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Search customers by name
     */
    public List<CustomerDTO> searchByName(String name) {
        log.info("Searching customers with name: {}", name);
        return customerRepository.findByNameContainingIgnoreCase(name)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Find customers by age range
     */
    public List<CustomerDTO> findByAgeRange(LocalDate startDate, LocalDate endDate) {
        log.info("Finding customers with birth date between {} and {}", startDate, endDate);
        return customerRepository.findByDateOfBirthBetween(startDate, endDate)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Find customers living in a city
     */
    public List<CustomerDTO> findByCity(Long cityId) {
        log.info("Finding customers in city ID: {}", cityId);
        return customerRepository.findByCity(cityId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Find customers in a country
     */
    public List<CustomerDTO> findByCountry(Long countryId) {
        log.info("Finding customers in country ID: {}", countryId);
        return customerRepository.findByCountry(countryId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Create new customer
     * Validates input and checks for duplicate NIC
     */
    @Transactional
    public CustomerDTO createCustomer(CustomerCreateUpdateDTO createDTO) {
        log.info("Creating new customer with NIC: {}", createDTO.getNic());
        
        // Check for duplicate NIC
        if (customerRepository.existsByNic(createDTO.getNic())) {
            log.warn("Attempt to create customer with duplicate NIC: {}", createDTO.getNic());
            throw new DuplicateNicException("Customer with NIC " + createDTO.getNic() + " already exists", 
                                           createDTO.getNic());
        }
        
        // Create customer entity
        Customer customer = Customer.builder()
            .name(createDTO.getName())
            .dateOfBirth(createDTO.getDateOfBirth())
            .nic(createDTO.getNic())
            .mobileNumbers(createDTO.getMobileNumbers())
            .build();
        
        // Add addresses
        if (createDTO.getAddresses() != null && !createDTO.getAddresses().isEmpty()) {
            for (CustomerCreateUpdateDTO.AddressCreateDTO addressDTO : createDTO.getAddresses()) {
                Address address = createAddress(customer, addressDTO);
                customer.addAddress(address);
            }
        }
        
        // Add family members if provided
        if (createDTO.getFamilyMemberIds() != null && !createDTO.getFamilyMemberIds().isEmpty()) {
            for (Long familyMemberId : createDTO.getFamilyMemberIds()) {
                Customer familyMember = customerRepository.findById(familyMemberId)
                    .orElseThrow(() -> new CustomerNotFoundException("Family member not found with ID: " + familyMemberId));
                customer.addFamilyMember(familyMember);
            }
        }
        
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully with ID: {}", savedCustomer.getId());
        return convertToDTO(savedCustomer);
    }
    
    /**
     * Update customer
     */
    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerCreateUpdateDTO updateDTO) {
        log.info("Updating customer with ID: {}", id);
        
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id, id));
        
        // Check for duplicate NIC if NIC is being changed
        if (!customer.getNic().equals(updateDTO.getNic()) && 
            customerRepository.existsByNic(updateDTO.getNic())) {
            throw new DuplicateNicException("Customer with NIC " + updateDTO.getNic() + " already exists", 
                                           updateDTO.getNic());
        }
        
        customer.setName(updateDTO.getName());
        customer.setDateOfBirth(updateDTO.getDateOfBirth());
        customer.setNic(updateDTO.getNic());
        customer.setMobileNumbers(updateDTO.getMobileNumbers());
        
        // Update addresses
        customer.getAddresses().clear();
        if (updateDTO.getAddresses() != null && !updateDTO.getAddresses().isEmpty()) {
            for (CustomerCreateUpdateDTO.AddressCreateDTO addressDTO : updateDTO.getAddresses()) {
                Address address = createAddress(customer, addressDTO);
                customer.addAddress(address);
            }
        }
        
        // Update family members
        customer.getFamilyMembers().clear();
        if (updateDTO.getFamilyMemberIds() != null && !updateDTO.getFamilyMemberIds().isEmpty()) {
            for (Long familyMemberId : updateDTO.getFamilyMemberIds()) {
                Customer familyMember = customerRepository.findById(familyMemberId)
                    .orElseThrow(() -> new CustomerNotFoundException("Family member not found with ID: " + familyMemberId));
                customer.addFamilyMember(familyMember);
            }
        }
        
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer updated successfully");
        return convertToDTO(updatedCustomer);
    }
    
    /**
     * Delete customer
     */
    @Transactional
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with ID: {}", id);
        
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with ID: " + id, id);
        }
        
        customerRepository.deleteById(id);
        log.info("Customer deleted successfully");
    }
    
    /**
     * Add mobile number to customer
     */
    @Transactional
    public CustomerDTO addMobileNumber(Long customerId, String mobileNumber) {
        log.info("Adding mobile number to customer ID: {}", customerId);
        
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId, customerId));
        
        customer.addMobileNumber(mobileNumber);
        customerRepository.save(customer);
        log.info("Mobile number added successfully");
        return convertToDTO(customer);
    }
    
    /**
     * Remove mobile number from customer
     */
    @Transactional
    public CustomerDTO removeMobileNumber(Long customerId, String mobileNumber) {
        log.info("Removing mobile number from customer ID: {}", customerId);
        
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId, customerId));
        
        customer.getMobileNumbers().remove(mobileNumber);
        customerRepository.save(customer);
        log.info("Mobile number removed successfully");
        return convertToDTO(customer);
    }
    
    /**
     * Helper method to create address from DTO
     */
    private Address createAddress(Customer customer, CustomerCreateUpdateDTO.AddressCreateDTO addressDTO) {
        City city = cityRepository.findById(addressDTO.getCityId())
            .orElseThrow(() -> new InvalidInputException("City not found with ID: " + addressDTO.getCityId(), 
                                                        "cityId", "Invalid city ID"));
        
        Country country = countryRepository.findById(addressDTO.getCountryId())
            .orElseThrow(() -> new InvalidInputException("Country not found with ID: " + addressDTO.getCountryId(), 
                                                        "countryId", "Invalid country ID"));
        
        return Address.builder()
            .line1(addressDTO.getLine1())
            .line2(addressDTO.getLine2())
            .city(city)
            .country(country)
            .customer(customer)
            .build();
    }
    
    /**
     * Convert Customer entity to DTO
     */
    private CustomerDTO convertToDTO(Customer customer) {
        return CustomerDTO.builder()
            .id(customer.getId())
            .name(customer.getName())
            .dateOfBirth(customer.getDateOfBirth())
            .nic(customer.getNic())
            .mobileNumbers(customer.getMobileNumbers())
            .addresses(customer.getAddresses() != null ? 
                customer.getAddresses().stream()
                    .map(this::convertAddressToDTO)
                    .collect(Collectors.toList()) : 
                null)
            .familyMembers(customer.getFamilyMembers() != null ? 
                customer.getFamilyMembers().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()) : 
                null)
            .build();
    }
    
    /**
     * Convert Address entity to DTO
     */
    private AddressDTO convertAddressToDTO(Address address) {
        return AddressDTO.builder()
            .id(address.getId())
            .line1(address.getLine1())
            .line2(address.getLine2())
            .city(com.example.demo.dto.CityDTO.builder()
                .id(address.getCity().getId())
                .name(address.getCity().getName())
                .build())
            .country(com.example.demo.dto.CountryDTO.builder()
                .id(address.getCountry().getId())
                .name(address.getCountry().getName())
                .build())
            .build();
    }
}