package com.example.demo.service;

import com.example.demo.dto.CountryDTO;
import com.example.demo.entity.Country;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.repository.CountryRepository;
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

@ExtendWith(MockitoExtension.class)
@DisplayName("Country Service Tests")
class CountryServiceTest {
    
    @Mock
    private CountryRepository countryRepository;
    
    @InjectMocks
    private CountryService countryService;
    
    private Country country;
    private CountryDTO countryDTO;
    
    @BeforeEach
    void setUp() {
        country = Country.builder()
            .id(1L)
            .name("Sri Lanka")
            .build();
        
        countryDTO = CountryDTO.builder()
            .id(1L)
            .name("Sri Lanka")
            .build();
    }
    
    @Test
    @DisplayName("Should get all countries successfully")
    void testGetAllCountries() {
        List<Country> countries = Arrays.asList(country);
        when(countryRepository.findAll()).thenReturn(countries);
        
        List<CountryDTO> result = countryService.getAllCountries();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sri Lanka", result.get(0).getName());
        verify(countryRepository, times(1)).findAll();
    }
    
    @Test
    @DisplayName("Should get country by ID successfully")
    void testGetCountryById() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        
        CountryDTO result = countryService.getCountryById(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Sri Lanka", result.getName());
        verify(countryRepository, times(1)).findById(1L);
    }
    
    @Test
    @DisplayName("Should throw exception when country not found by ID")
    void testGetCountryByIdNotFound() {
        when(countryRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(InvalidInputException.class, 
            () -> countryService.getCountryById(999L));
        verify(countryRepository, times(1)).findById(999L);
    }
    
    @Test
    @DisplayName("Should get country by name successfully")
    void testGetCountryByName() {
        when(countryRepository.findByName("Sri Lanka")).thenReturn(Optional.of(country));
        
        CountryDTO result = countryService.getCountryByName("Sri Lanka");
        
        assertNotNull(result);
        assertEquals("Sri Lanka", result.getName());
        verify(countryRepository, times(1)).findByName("Sri Lanka");
    }
    
    @Test
    @DisplayName("Should create country successfully")
    void testCreateCountry() {
        Country newCountry = Country.builder()
            .id(1L)
            .name("India")
            .build();
        
        when(countryRepository.save(any(Country.class))).thenReturn(newCountry);
        
        CountryDTO result = countryService.createCountry(
            CountryDTO.builder().name("India").build());
        
        assertNotNull(result);
        assertEquals("India", result.getName());
        verify(countryRepository, times(1)).save(any(Country.class));
    }
    
    @Test
    @DisplayName("Should throw exception when creating country with empty name")
    void testCreateCountryWithEmptyName() {
        assertThrows(InvalidInputException.class, 
            () -> countryService.createCountry(
                CountryDTO.builder().name("").build()));
    }
    
    @Test
    @DisplayName("Should update country successfully")
    void testUpdateCountry() {
        Country updatedCountry = Country.builder()
            .id(1L)
            .name("Sri Lanka Updated")
            .build();
        
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(countryRepository.save(any(Country.class))).thenReturn(updatedCountry);
        
        CountryDTO result = countryService.updateCountry(1L, 
            CountryDTO.builder().name("Sri Lanka Updated").build());
        
        assertNotNull(result);
        assertEquals("Sri Lanka Updated", result.getName());
        verify(countryRepository, times(1)).findById(1L);
        verify(countryRepository, times(1)).save(any(Country.class));
    }
    
    @Test
    @DisplayName("Should delete country successfully")
    void testDeleteCountry() {
        when(countryRepository.existsById(1L)).thenReturn(true);
        
        countryService.deleteCountry(1L);
        
        verify(countryRepository, times(1)).existsById(1L);
        verify(countryRepository, times(1)).deleteById(1L);
    }
    
    @Test
    @DisplayName("Should throw exception when deleting non-existent country")
    void testDeleteCountryNotFound() {
        when(countryRepository.existsById(999L)).thenReturn(false);
        
        assertThrows(InvalidInputException.class, 
            () -> countryService.deleteCountry(999L));
        verify(countryRepository, times(1)).existsById(999L);
        verify(countryRepository, never()).deleteById(999L);
    }
}