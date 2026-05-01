package com.example.demo.controller;

import com.example.demo.dto.CityDTO;
import com.example.demo.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for City management
 * Base URL: /api/v1/cities
 */
@RestController
@RequestMapping("/api/v1/cities")
@RequiredArgsConstructor
@Slf4j
public class CityController {
    
    private final CityService cityService;
    
    /**
     * GET /api/v1/cities
     * Retrieve all cities
     * @return list of all cities
     */
    @GetMapping
    public ResponseEntity<List<CityDTO>> getAllCities() {
        log.info("GET request: Retrieve all cities");
        List<CityDTO> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }
    
    /**
     * GET /api/v1/cities/{id}
     * Retrieve city by ID
     * @param id the city ID
     * @return city with specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> getCityById(@PathVariable Long id) {
        log.info("GET request: Retrieve city with ID: {}", id);
        CityDTO city = cityService.getCityById(id);
        return ResponseEntity.ok(city);
    }
    
    /**
     * GET /api/v1/cities/search?name={name}
     * Search city by name
     * @param name the city name
     * @return city matching the name
     */
    @GetMapping("/search")
    public ResponseEntity<CityDTO> searchByName(@RequestParam String name) {
        log.info("GET request: Search city by name: {}", name);
        CityDTO city = cityService.getCityByName(name);
        return ResponseEntity.ok(city);
    }
    
    /**
     * POST /api/v1/cities
     * Create new city
     * @param cityDTO the city data
     * @return created city with HTTP 201
     */
    @PostMapping
    public ResponseEntity<CityDTO> createCity(@RequestBody CityDTO cityDTO) {
        log.info("POST request: Create city with name: {}", cityDTO.getName());
        CityDTO createdCity = cityService.createCity(cityDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCity);
    }
    
    /**
     * PUT /api/v1/cities/{id}
     * Update city
     * @param id the city ID
     * @param cityDTO the updated city data
     * @return updated city
     */
    @PutMapping("/{id}")
    public ResponseEntity<CityDTO> updateCity(
            @PathVariable Long id,
            @RequestBody CityDTO cityDTO) {
        log.info("PUT request: Update city with ID: {}", id);
        CityDTO updatedCity = cityService.updateCity(id, cityDTO);
        return ResponseEntity.ok(updatedCity);
    }
    
    /**
     * DELETE /api/v1/cities/{id}
     * Delete city
     * @param id the city ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        log.info("DELETE request: Delete city with ID: {}", id);
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }
}