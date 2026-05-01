package com.example.demo.service;

import com.example.demo.dto.CountryDTO;
import com.example.demo.entity.Country;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.repository.CountryRepository;
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
public class CountryService {
    
    private final CountryRepository countryRepository;
    
    /**
     * Get all countries
     */
    public List<CountryDTO> getAllCountries() {
        log.info("Fetching all countries");
        return countryRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get country by ID
     */
    public CountryDTO getCountryById(Long id) {
        log.info("Fetching country with ID: {}", id);
        Country country = countryRepository.findById(id)
            .orElseThrow(() -> new InvalidInputException("Country not found with ID: " + id));
        return convertToDTO(country);
    }
    
    /**
     * Get country by name
     */
    public CountryDTO getCountryByName(String name) {
        log.info("Fetching country with name: {}", name);
        Country country = countryRepository.findByName(name)
            .orElseThrow(() -> new InvalidInputException("Country not found with name: " + name));
        return convertToDTO(country);
    }
    
    /**
     * Create new country
     */
    @Transactional
    public CountryDTO createCountry(CountryDTO countryDTO) {
        log.info("Creating new country: {}", countryDTO.getName());
        
        if (countryDTO.getName() == null || countryDTO.getName().isBlank()) {
            throw new InvalidInputException("Country name cannot be empty", "name", "Name is required");
        }
        
        Country country = Country.builder()
            .name(countryDTO.getName())
            .build();
        
        Country savedCountry = countryRepository.save(country);
        log.info("Country created successfully with ID: {}", savedCountry.getId());
        return convertToDTO(savedCountry);
    }
    
    /**
     * Update country
     */
    @Transactional
    public CountryDTO updateCountry(Long id, CountryDTO countryDTO) {
        log.info("Updating country with ID: {}", id);
        
        Country country = countryRepository.findById(id)
            .orElseThrow(() -> new InvalidInputException("Country not found with ID: " + id));
        
        country.setName(countryDTO.getName());
        Country updatedCountry = countryRepository.save(country);
        log.info("Country updated successfully");
        return convertToDTO(updatedCountry);
    }
    
    /**
     * Delete country
     */
    @Transactional
    public void deleteCountry(Long id) {
        log.info("Deleting country with ID: {}", id);
        
        if (!countryRepository.existsById(id)) {
            throw new InvalidInputException("Country not found with ID: " + id);
        }
        
        countryRepository.deleteById(id);
        log.info("Country deleted successfully");
    }
    
    /**
     * Convert Country entity to DTO
     */
    private CountryDTO convertToDTO(Country country) {
        return CountryDTO.builder()
            .id(country.getId())
            .name(country.getName())
            .build();
    }
}