package com.example.demo.service;

import com.example.demo.dto.CityDTO;
import com.example.demo.entity.City;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.repository.CityRepository;
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
public class CityService {
    
    private final CityRepository cityRepository;
    
    /**
     * Get all cities
     */
    public List<CityDTO> getAllCities() {
        log.info("Fetching all cities");
        return cityRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get city by ID
     */
    public CityDTO getCityById(Long id) {
        log.info("Fetching city with ID: {}", id);
        City city = cityRepository.findById(id)
            .orElseThrow(() -> new InvalidInputException("City not found with ID: " + id));
        return convertToDTO(city);
    }
    
    /**
     * Get city by name
     */
    public CityDTO getCityByName(String name) {
        log.info("Fetching city with name: {}", name);
        City city = cityRepository.findByName(name)
            .orElseThrow(() -> new InvalidInputException("City not found with name: " + name));
        return convertToDTO(city);
    }
    
    /**
     * Create new city
     */
    @Transactional
    public CityDTO createCity(CityDTO cityDTO) {
        log.info("Creating new city: {}", cityDTO.getName());
        
        if (cityDTO.getName() == null || cityDTO.getName().isBlank()) {
            throw new InvalidInputException("City name cannot be empty", "name", "Name is required");
        }
        
        City city = City.builder()
            .name(cityDTO.getName())
            .build();
        
        City savedCity = cityRepository.save(city);
        log.info("City created successfully with ID: {}", savedCity.getId());
        return convertToDTO(savedCity);
    }
    
    /**
     * Update city
     */
    @Transactional
    public CityDTO updateCity(Long id, CityDTO cityDTO) {
        log.info("Updating city with ID: {}", id);
        
        City city = cityRepository.findById(id)
            .orElseThrow(() -> new InvalidInputException("City not found with ID: " + id));
        
        city.setName(cityDTO.getName());
        City updatedCity = cityRepository.save(city);
        log.info("City updated successfully");
        return convertToDTO(updatedCity);
    }
    
    /**
     * Delete city
     */
    @Transactional
    public void deleteCity(Long id) {
        log.info("Deleting city with ID: {}", id);
        
        if (!cityRepository.existsById(id)) {
            throw new InvalidInputException("City not found with ID: " + id);
        }
        
        cityRepository.deleteById(id);
        log.info("City deleted successfully");
    }
    
    /**
     * Convert City entity to DTO
     */
    private CityDTO convertToDTO(City city) {
        return CityDTO.builder()
            .id(city.getId())
            .name(city.getName())
            .build();
    }
}