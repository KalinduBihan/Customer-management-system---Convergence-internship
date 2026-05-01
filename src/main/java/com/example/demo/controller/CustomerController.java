package com.example.demo.controller;

import com.example.demo.dto.CustomerCreateUpdateDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for Customer management
 * Base URL: /api/v1/customers
 * 
 * Provides comprehensive CRUD operations for customers including:
 * - Create, Read, Update, Delete customers
 * - Search customers by various criteria
 * - Manage customer mobile numbers
 * - Handle customer relationships (family members, addresses)
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class CustomerController {
    
    private final CustomerService customerService;
    
    /**
     * GET /api/v1/customers/{id}
     * Retrieve customer by ID with all details
     * 
     * @param id the customer ID
     * @return customer with all relationships (addresses, family members)
     * @throws CustomerNotFoundException if customer not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        log.info("GET request: Retrieve customer with ID: {}", id);
        CustomerDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }
    
    /**
     * GET /api/v1/customers/nic/{nic}
     * Retrieve customer by NIC (National ID Card)
     * 
     * @param nic the customer's NIC number
     * @return customer matching the NIC
     * @throws CustomerNotFoundException if customer not found
     */
    @GetMapping("/nic/{nic}")
    public ResponseEntity<CustomerDTO> getCustomerByNic(@PathVariable String nic) {
        log.info("GET request: Retrieve customer with NIC: {}", nic);
        CustomerDTO customer = customerService.getCustomerByNic(nic);
        return ResponseEntity.ok(customer);
    }
    
    /**
     * GET /api/v1/customers
     * Retrieve all customers with their addresses
     * 
     * @return list of all customers
     */
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        log.info("GET request: Retrieve all customers");
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
    
    /**
     * GET /api/v1/customers/search/by-name?name={name}
     * Search customers by name (case-insensitive, partial match)
     * 
     * @param name the customer name to search (partial match allowed)
     * @return list of customers matching the name
     */
    @GetMapping("/search/by-name")
    public ResponseEntity<List<CustomerDTO>> searchByName(@RequestParam String name) {
        log.info("GET request: Search customers by name: {}", name);
        List<CustomerDTO> customers = customerService.searchByName(name);
        return ResponseEntity.ok(customers);
    }
    
    /**
     * GET /api/v1/customers/search/by-age?startDate={date}&endDate={date}
     * Find customers by birth date range
     * 
     * @param startDate the start date (format: yyyy-MM-dd)
     * @param endDate the end date (format: yyyy-MM-dd)
     * @return list of customers with birth dates in the range
     */
    @GetMapping("/search/by-age")
    public ResponseEntity<List<CustomerDTO>> findByAgeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET request: Find customers with birth date between {} and {}", startDate, endDate);
        List<CustomerDTO> customers = customerService.findByAgeRange(startDate, endDate);
        return ResponseEntity.ok(customers);
    }
    
    /**
     * GET /api/v1/customers/search/by-city?cityId={cityId}
     * Find customers living in a specific city
     * 
     * @param cityId the city ID
     * @return list of customers in the specified city
     */
    @GetMapping("/search/by-city")
    public ResponseEntity<List<CustomerDTO>> findByCity(@RequestParam Long cityId) {
        log.info("GET request: Find customers in city ID: {}", cityId);
        List<CustomerDTO> customers = customerService.findByCity(cityId);
        return ResponseEntity.ok(customers);
    }
    
    /**
     * GET /api/v1/customers/search/by-country?countryId={countryId}
     * Find customers in a specific country
     * 
     * @param countryId the country ID
     * @return list of customers in the specified country
     */
    @GetMapping("/search/by-country")
    public ResponseEntity<List<CustomerDTO>> findByCountry(@RequestParam Long countryId) {
        log.info("GET request: Find customers in country ID: {}", countryId);
        List<CustomerDTO> customers = customerService.findByCountry(countryId);
        return ResponseEntity.ok(customers);
    }
    
    /**
     * POST /api/v1/customers
     * Create a new customer
     * 
     * Request body example:
     * {
     *   "name": "John Doe",
     *   "dateOfBirth": "1990-05-15",
     *   "nic": "991234567V",
     *   "mobileNumbers": ["0771234567", "0772345678"],
     *   "addresses": [
     *     {
     *       "line1": "123 Main Street",
     *       "line2": "Apt 4B",
     *       "cityId": 1,
     *       "countryId": 1
     *     }
     *   ],
     *   "familyMemberIds": [2, 3]
     * }
     * 
     * @param createDTO the customer creation data (validated)
     * @return created customer with HTTP 201
     * @throws DuplicateNicException if NIC already exists
     * @throws InvalidInputException if input validation fails
     */
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerCreateUpdateDTO createDTO) {
        log.info("POST request: Create customer with NIC: {}", createDTO.getNic());
        CustomerDTO createdCustomer = customerService.createCustomer(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }
    
    /**
     * PUT /api/v1/customers/{id}
     * Update an existing customer
     * 
     * @param id the customer ID
     * @param updateDTO the updated customer data (validated)
     * @return updated customer
     * @throws CustomerNotFoundException if customer not found
     * @throws DuplicateNicException if NIC is being changed to an existing NIC
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerCreateUpdateDTO updateDTO) {
        log.info("PUT request: Update customer with ID: {}", id);
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, updateDTO);
        return ResponseEntity.ok(updatedCustomer);
    }
    
    /**
     * DELETE /api/v1/customers/{id}
     * Delete a customer and all associated data
     * 
     * @param id the customer ID
     * @return HTTP 204 No Content
     * @throws CustomerNotFoundException if customer not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.info("DELETE request: Delete customer with ID: {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * POST /api/v1/customers/{id}/mobile-numbers
     * Add a mobile number to a customer
     * 
     * Request body example:
     * {
     *   "mobileNumber": "0771234567"
     * }
     * 
     * @param id the customer ID
     * @param mobileNumber the mobile number to add
     * @return updated customer
     * @throws CustomerNotFoundException if customer not found
     */
    @PostMapping("/{id}/mobile-numbers")
    public ResponseEntity<CustomerDTO> addMobileNumber(
            @PathVariable Long id,
            @RequestParam String mobileNumber) {
        log.info("POST request: Add mobile number to customer ID: {}", id);
        CustomerDTO updatedCustomer = customerService.addMobileNumber(id, mobileNumber);
        return ResponseEntity.ok(updatedCustomer);
    }
    
    /**
     * DELETE /api/v1/customers/{id}/mobile-numbers
     * Remove a mobile number from a customer
     * 
     * @param id the customer ID
     * @param mobileNumber the mobile number to remove
     * @return updated customer
     * @throws CustomerNotFoundException if customer not found
     */
    @DeleteMapping("/{id}/mobile-numbers")
    public ResponseEntity<CustomerDTO> removeMobileNumber(
            @PathVariable Long id,
            @RequestParam String mobileNumber) {
        log.info("DELETE request: Remove mobile number from customer ID: {}", id);
        CustomerDTO updatedCustomer = customerService.removeMobileNumber(id, mobileNumber);
        return ResponseEntity.ok(updatedCustomer);
    }
}