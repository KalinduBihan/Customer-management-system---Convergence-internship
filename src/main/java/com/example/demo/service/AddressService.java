package com.example.demo.service;

import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.CityDTO;
import com.example.demo.dto.CountryDTO;
import com.example.demo.entity.Address;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AddressService {
    
    private final AddressRepository addressRepository;
    
    /**
     * Get address by ID
     */
    public AddressDTO getAddressById(Long id) {
        log.info("Fetching address with ID: {}", id);
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new InvalidInputException("Address not found with ID: " + id));
        return convertToDTO(address);
    }
    
    /**
     * Get all addresses for a customer
     */
    public List<AddressDTO> getAddressesByCustomerId(Long customerId) {
        log.info("Fetching addresses for customer ID: {}", customerId);
        return addressRepository.findByCustomerId(customerId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Delete address
     */
    @Transactional
    public void deleteAddress(Long id) {
        log.info("Deleting address with ID: {}", id);
        
        if (!addressRepository.existsById(id)) {
            throw new InvalidInputException("Address not found with ID: " + id);
        }
        
        addressRepository.deleteById(id);
        log.info("Address deleted successfully");
    }
    
    /**
     * Convert Address entity to DTO
     */
    private AddressDTO convertToDTO(Address address) {
        return AddressDTO.builder()
            .id(address.getId())
            .line1(address.getLine1())
            .line2(address.getLine2())
            .city(CityDTO.builder()
                .id(address.getCity().getId())
                .name(address.getCity().getName())
                .build())
            .country(CountryDTO.builder()
                .id(address.getCountry().getId())
                .name(address.getCountry().getName())
                .build())
            .build();
    }
}
