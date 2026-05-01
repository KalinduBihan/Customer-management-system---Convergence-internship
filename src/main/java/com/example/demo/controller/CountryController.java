package com.example.demo.controller;

import com.example.demo.dto.CountryDTO;
import com.example.demo.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Country management
 * Base URL: /api/v1/countries
 */
@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
@Slf4j
public class CountryController {
    
    private final CountryService countryService;
    
    /**
     * GET /api/v1/countries
     * Retrieve all countries
     * @return list of all countries
     */
    @GetMapping
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        log.info("GET request: Retrieve all countries");
        List<CountryDTO> countries = countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }
    
    /**
     * GET /api/v1/countries/{id}
     * Retrieve country by ID
     * @param id the country ID
     * @return country with specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CountryDTO> getCountryById(@PathVariable Long id) {
        log.info("GET request: Retrieve country with ID: {}", id);
        CountryDTO country = countryService.getCountryById(id);
        return ResponseEntity.ok(country);
    }
    
    /**
     * GET /api/v1/countries/search?name={name}
     * Search country by name
     * @param name the country name
     * @return country matching the name
     */
    @GetMapping("/search")
    public ResponseEntity<CountryDTO> searchByName(@RequestParam String name) {
        log.info("GET request: Search country by name: {}", name);
        CountryDTO country = countryService.getCountryByName(name);
        return ResponseEntity.ok(country);
    }
    
    /**
     * POST /api/v1/countries
     * Create new country
     * @param countryDTO the country data
     * @return created country with HTTP 201
     */
    @PostMapping
    public ResponseEntity<CountryDTO> createCountry(@RequestBody CountryDTO countryDTO) {
        log.info("POST request: Create country with name: {}", countryDTO.getName());
        CountryDTO createdCountry = countryService.createCountry(countryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCountry);
    }
    
    /**
     * PUT /api/v1/countries/{id}
     * Update country
     * @param id the country ID
     * @param countryDTO the updated country data
     * @return updated country
     */
    @PutMapping("/{id}")
    public ResponseEntity<CountryDTO> updateCountry(
            @PathVariable Long id,
            @RequestBody CountryDTO countryDTO) {
        log.info("PUT request: Update country with ID: {}", id);
        CountryDTO updatedCountry = countryService.updateCountry(id, countryDTO);
        return ResponseEntity.ok(updatedCountry);
    }
    
    /**
     * DELETE /api/v1/countries/{id}
     * Delete country
     * @param id the country ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        log.info("DELETE request: Delete country with ID: {}", id);
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }
}