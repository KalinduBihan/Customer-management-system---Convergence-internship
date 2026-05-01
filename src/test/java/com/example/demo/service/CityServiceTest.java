package com.example.demo.service;

import com.example.demo.dto.CityDTO;
import com.example.demo.entity.City;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CityService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("City Service Tests")
class CityServiceTest {
    
    @Mock
    private CityRepository cityRepository;
    
    @InjectMocks
    private CityService cityService;
    
    private City city;
    
    @BeforeEach
    void setUp() {
        city = City.builder()
            .id(1L)
            .name("Colombo")
            .build();
    }
    
    @Test
    @DisplayName("Should get all cities successfully")
    void testGetAllCities() {
        // Arrange
        List<City> cities = Arrays.asList(city);
        when(cityRepository.findAll()).thenReturn(cities);
        
        // Act
        List<CityDTO> result = cityService.getAllCities();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Colombo", result.get(0).getName());
    }
    
    @Test
    @DisplayName("Should get city by ID successfully")
    void testGetCityById() {
        // Arrange
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        
        // Act
        CityDTO result = cityService.getCityById(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals("Colombo", result.getName());
    }
    
    @Test
    @DisplayName("Should throw exception when city not found")
    void testGetCityByIdNotFound() {
        // Arrange
        when(cityRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(InvalidInputException.class, 
            () -> cityService.getCityById(999L));
    }
    
    @Test
    @DisplayName("Should create city successfully")
    void testCreateCity() {
        // Arrange
        when(cityRepository.save(any(City.class))).thenReturn(city);
        
        // Act
        CityDTO result = cityService.createCity(
            CityDTO.builder().name("Colombo").build());
        
        // Assert
        assertNotNull(result);
        assertEquals("Colombo", result.getName());
    }
}