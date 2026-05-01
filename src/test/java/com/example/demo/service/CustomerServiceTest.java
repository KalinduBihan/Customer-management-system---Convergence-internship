package com.example.demo.service;

import com.example.demo.dto.CustomerCreateUpdateDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.Address;
import com.example.demo.entity.City;
import com.example.demo.entity.Country;
import com.example.demo.entity.Customer;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.exception.DuplicateNicException;
import com.example.demo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CustomerService
 * Tests all customer-related business logic
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Service Tests")
class CustomerServiceTest {
    
    @Mock
    private CustomerRepository customerRepository;
    
    @Mock
    private CityRepository cityRepository;
    
    @Mock
    private CountryRepository countryRepository;
    
    @Mock
    private AddressRepository addressRepository;
    
    @InjectMocks
    private CustomerService customerService;
    
    private Customer customer;
    private City city;
    private Country country;
    private Address address;
    
    @BeforeEach
    void setUp() {
        country = Country.builder()
            .id(1L)
            .name("Sri Lanka")
            .build();
        
        city = City.builder()
            .id(1L)
            .name("Colombo")
            .build();
        
        customer = Customer.builder()
            .id(1L)
            .name("John Doe")
            .dateOfBirth(LocalDate.of(1990, 5, 15))
            .nic("991234567V")
            .mobileNumbers(Arrays.asList("0771234567", "0772345678"))
            .build();
        
        address = Address.builder()
            .id(1L)
            .line1("123 Main Street")
            .line2("Apt 4B")
            .city(city)
            .country(country)
            .customer(customer)
            .build();
        
        customer.setAddresses(Arrays.asList(address));
    }
    
    @Test
    @DisplayName("Should get customer by ID with all details")
    void testGetCustomerById() {
        // Arrange
        when(customerRepository.findByIdWithDetails(1L))
            .thenReturn(Optional.of(customer));
        
        // Act
        CustomerDTO result = customerService.getCustomerById(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("991234567V", result.getNic());
        assertEquals(2, result.getMobileNumbers().size());
        verify(customerRepository, times(1)).findByIdWithDetails(1L);
    }
    
    @Test
    @DisplayName("Should throw exception when customer not found")
    void testGetCustomerByIdNotFound() {
        // Arrange
        when(customerRepository.findByIdWithDetails(999L))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(CustomerNotFoundException.class, 
            () -> customerService.getCustomerById(999L));
    }
    
    @Test
    @DisplayName("Should get customer by NIC successfully")
    void testGetCustomerByNic() {
        // Arrange
        when(customerRepository.findByNic("991234567V"))
            .thenReturn(Optional.of(customer));
        
        // Act
        CustomerDTO result = customerService.getCustomerByNic("991234567V");
        
        // Assert
        assertNotNull(result);
        assertEquals("991234567V", result.getNic());
    }
    
    @Test
    @DisplayName("Should get all customers successfully")
    void testGetAllCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findAllWithAddresses()).thenReturn(customers);
        
        // Act
        List<CustomerDTO> result = customerService.getAllCustomers();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }
    
    @Test
    @DisplayName("Should search customers by name")
    void testSearchByName() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByNameContainingIgnoreCase("John"))
            .thenReturn(customers);
        
        // Act
        List<CustomerDTO> result = customerService.searchByName("John");
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    @DisplayName("Should create customer successfully")
    void testCreateCustomer() {
        // Arrange
        CustomerCreateUpdateDTO createDTO = CustomerCreateUpdateDTO.builder()
            .name("John Doe")
            .dateOfBirth(LocalDate.of(1990, 5, 15))
            .nic("991234567V")
            .mobileNumbers(Arrays.asList("0771234567"))
            .addresses(Arrays.asList(
                CustomerCreateUpdateDTO.AddressCreateDTO.builder()
                    .line1("123 Main Street")
                    .cityId(1L)
                    .countryId(1L)
                    .build()
            ))
            .build();
        
        when(customerRepository.existsByNic("991234567V")).thenReturn(false);
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        
        // Act
        CustomerDTO result = customerService.createCustomer(createDTO);
        
        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("991234567V", result.getNic());
        verify(customerRepository, times(1)).existsByNic("991234567V");
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
    
    @Test
    @DisplayName("Should throw exception when creating customer with duplicate NIC")
    void testCreateCustomerWithDuplicateNic() {
        // Arrange
        CustomerCreateUpdateDTO createDTO = CustomerCreateUpdateDTO.builder()
            .name("John Doe")
            .dateOfBirth(LocalDate.of(1990, 5, 15))
            .nic("991234567V")
            .mobileNumbers(Arrays.asList("0771234567"))
            .addresses(Arrays.asList(
                CustomerCreateUpdateDTO.AddressCreateDTO.builder()
                    .line1("123 Main Street")
                    .cityId(1L)
                    .countryId(1L)
                    .build()
            ))
            .build();
        
        when(customerRepository.existsByNic("991234567V")).thenReturn(true);
        
        // Act & Assert
        assertThrows(DuplicateNicException.class, 
            () -> customerService.createCustomer(createDTO));
        verify(customerRepository, never()).save(any(Customer.class));
    }
    
    @Test
    @DisplayName("Should update customer successfully")
    void testUpdateCustomer() {
        // Arrange
        CustomerCreateUpdateDTO updateDTO = CustomerCreateUpdateDTO.builder()
            .name("John Doe Updated")
            .dateOfBirth(LocalDate.of(1990, 5, 15))
            .nic("991234567V")
            .mobileNumbers(Arrays.asList("0771234567"))
            .addresses(Arrays.asList(
                CustomerCreateUpdateDTO.AddressCreateDTO.builder()
                    .line1("456 New Street")
                    .cityId(1L)
                    .countryId(1L)
                    .build()
            ))
            .build();
        
        Customer updatedCustomer = Customer.builder()
            .id(1L)
            .name("John Doe Updated")
            .dateOfBirth(LocalDate.of(1990, 5, 15))
            .nic("991234567V")
            .mobileNumbers(Arrays.asList("0771234567"))
            .build();
        
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByNic("991234567V")).thenReturn(true);
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);
        
        // Act
        CustomerDTO result = customerService.updateCustomer(1L, updateDTO);
        
        // Assert
        assertNotNull(result);
        assertEquals("John Doe Updated", result.getName());
    }
    
    @Test
    @DisplayName("Should delete customer successfully")
    void testDeleteCustomer() {
        // Arrange
        when(customerRepository.existsById(1L)).thenReturn(true);
        
        // Act
        customerService.deleteCustomer(1L);
        
        // Assert
        verify(customerRepository, times(1)).existsById(1L);
        verify(customerRepository, times(1)).deleteById(1L);
    }
    
    @Test
    @DisplayName("Should throw exception when deleting non-existent customer")
    void testDeleteCustomerNotFound() {
        // Arrange
        when(customerRepository.existsById(999L)).thenReturn(false);
        
        // Act & Assert
        assertThrows(CustomerNotFoundException.class, 
            () -> customerService.deleteCustomer(999L));
        verify(customerRepository, never()).deleteById(999L);
    }
    
    @Test
    @DisplayName("Should add mobile number successfully")
    void testAddMobileNumber() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        
        // Act
        CustomerDTO result = customerService.addMobileNumber(1L, "0770000000");
        
        // Assert
        assertNotNull(result);
        assertTrue(result.getMobileNumbers().contains("0770000000"));
    }
    
    @Test
    @DisplayName("Should remove mobile number successfully")
    void testRemoveMobileNumber() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        
        // Act
        customerService.removeMobileNumber(1L, "0771234567");
        
        // Assert
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
    
    @Test
    @DisplayName("Should find customers by city")
    void testFindByCity() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCity(1L)).thenReturn(customers);
        
        // Act
        List<CustomerDTO> result = customerService.findByCity(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}