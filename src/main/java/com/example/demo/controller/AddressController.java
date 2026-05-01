package com.example.demo.controller;

import com.example.demo.dto.AddressDTO;
import com.example.demo.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Address management
 * Base URL: /api/v1/addresses
 */
@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
@Slf4j
public class AddressController {
    
    private final AddressService addressService;
    
    /**
     * GET /api/v1/addresses/{id}
     * Retrieve address by ID
     * @param id the address ID
     * @return address with specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long id) {
        log.info("GET request: Retrieve address with ID: {}", id);
        AddressDTO address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }
    
    /**
     * GET /api/v1/addresses/customer/{customerId}
     * Retrieve all addresses for a customer
     * @param customerId the customer ID
     * @return list of addresses for the customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AddressDTO>> getAddressesByCustomerId(@PathVariable Long customerId) {
        log.info("GET request: Retrieve addresses for customer ID: {}", customerId);
        List<AddressDTO> addresses = addressService.getAddressesByCustomerId(customerId);
        return ResponseEntity.ok(addresses);
    }
    
    /**
     * DELETE /api/v1/addresses/{id}
     * Delete address
     * @param id the address ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        log.info("DELETE request: Delete address with ID: {}", id);
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}