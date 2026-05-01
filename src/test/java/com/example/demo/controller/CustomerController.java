package com.example.demo.controller;

import com.example.demo.dto.CustomerCreateUpdateDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // ✅ Get customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        CustomerDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    // ✅ Get all customers
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // ✅ Get customer by NIC
    @GetMapping("/nic/{nic}")
    public ResponseEntity<CustomerDTO> getCustomerByNic(@PathVariable String nic) {
        return ResponseEntity.ok(customerService.getCustomerByNic(nic));
    }

    // ✅ Search customers by name
    @GetMapping("/search/by-name")
    public ResponseEntity<List<CustomerDTO>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(customerService.searchByName(name));
    }

    // ✅ Create customer
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(
            @Valid @RequestBody CustomerCreateUpdateDTO dto) {

        CustomerDTO created = customerService.createCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ✅ Update customer
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerCreateUpdateDTO dto) {

        CustomerDTO updated = customerService.updateCustomer(id, dto);
        return ResponseEntity.ok(updated);
    }

    // ✅ Delete customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Add mobile number
    @PostMapping("/{id}/mobile-numbers")
    public ResponseEntity<CustomerDTO> addMobileNumber(
            @PathVariable Long id,
            @RequestParam String mobileNumber) {

        return ResponseEntity.ok(customerService.addMobileNumber(id, mobileNumber));
    }

    // ✅ Remove mobile number
    @DeleteMapping("/{id}/mobile-numbers")
    public ResponseEntity<CustomerDTO> removeMobileNumber(
            @PathVariable Long id,
            @RequestParam String mobileNumber) {

        return ResponseEntity.ok(customerService.removeMobileNumber(id, mobileNumber));
    }
}
// package com.example.demo.controller;

// import com.example.demo.dto.CustomerCreateUpdateDTO;
// import com.example.demo.dto.CustomerDTO;
// import com.example.demo.service.CustomerService;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.webmvc.WebMvcTest;
// import org.springframework.boot.test.mock.mockbean.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import java.time.LocalDate;
// import java.util.Arrays;
// import java.util.List;

// import static org.hamcrest.Matchers.hasSize;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// /**
//  * Integration tests for CustomerController
//  * Tests API endpoints with mocked service layer
//  */
// @WebMvcTest(CustomerController.class)
// @DisplayName("Customer Controller Tests")
// class CustomerControllerTest {
    
//     @Autowired
//     private MockMvc mockMvc;
    
//     @MockBean
//     private CustomerService customerService;
    
//     @Autowired
//     private ObjectMapper objectMapper;
    
//     private CustomerDTO customerDTO;
    
//     @BeforeEach
//     void setUp() {
//         customerDTO = CustomerDTO.builder()
//             .id(1L)
//             .name("John Doe")
//             .dateOfBirth(LocalDate.of(1990, 5, 15))
//             .nic("991234567V")
//             .mobileNumbers(Arrays.asList("0771234567", "0772345678"))
//             .addresses(Arrays.asList())
//             .familyMembers(Arrays.asList())
//             .build();
//     }
    
//     @Test
//     @DisplayName("Should return customer by ID with status 200")
//     void testGetCustomerById() throws Exception {
//         // Arrange
//         when(customerService.getCustomerById(1L)).thenReturn(customerDTO);
        
//         // Act & Assert
//         mockMvc.perform(get("/api/v1/customers/1")
//             .contentType(MediaType.APPLICATION_JSON))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.id").value(1L))
//             .andExpect(jsonPath("$.name").value("John Doe"))
//             .andExpect(jsonPath("$.nic").value("991234567V"));
        
//         verify(customerService, times(1)).getCustomerById(1L);
//     }
    
//     @Test
//     @DisplayName("Should return 404 when customer not found")
//     void testGetCustomerByIdNotFound() throws Exception {
//         // Arrange
//         when(customerService.getCustomerById(999L))
//             .thenThrow(new com.example.demo.exception.CustomerNotFoundException(
//                 "Customer not found with ID: 999", 999L));
        
//         // Act & Assert
//         mockMvc.perform(get("/api/v1/customers/999")
//             .contentType(MediaType.APPLICATION_JSON))
//             .andExpect(status().isNotFound());
//     }
    
//     @Test
//     @DisplayName("Should return all customers with status 200")
//     void testGetAllCustomers() throws Exception {
//         // Arrange
//         List<CustomerDTO> customers = Arrays.asList(customerDTO);
//         when(customerService.getAllCustomers()).thenReturn(customers);
        
//         // Act & Assert
//         mockMvc.perform(get("/api/v1/customers")
//             .contentType(MediaType.APPLICATION_JSON))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$", hasSize(1)))
//             .andExpect(jsonPath("$[0].name").value("John Doe"));
        
//         verify(customerService, times(1)).getAllCustomers();
//     }
    
//     @Test
//     @DisplayName("Should get customer by NIC successfully")
//     void testGetCustomerByNic() throws Exception {
//         // Arrange
//         when(customerService.getCustomerByNic("991234567V")).thenReturn(customerDTO);
        
//         // Act & Assert
//         mockMvc.perform(get("/api/v1/customers/nic/991234567V")
//             .contentType(MediaType.APPLICATION_JSON))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.nic").value("991234567V"));
//     }
    
//     @Test
//     @DisplayName("Should search customers by name")
//     void testSearchByName() throws Exception {
//         // Arrange
//         List<CustomerDTO> customers = Arrays.asList(customerDTO);
//         when(customerService.searchByName("John")).thenReturn(customers);
        
//         // Act & Assert
//         mockMvc.perform(get("/api/v1/customers/search/by-name?name=John")
//             .contentType(MediaType.APPLICATION_JSON))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$", hasSize(1)));
//     }
    
//     @Test
//     @DisplayName("Should create customer with status 201")
//     void testCreateCustomer() throws Exception {
//         // Arrange
//         CustomerCreateUpdateDTO createDTO = CustomerCreateUpdateDTO.builder()
//             .name("John Doe")
//             .dateOfBirth(LocalDate.of(1990, 5, 15))
//             .nic("991234567V")
//             .mobileNumbers(Arrays.asList("0771234567"))
//             .addresses(Arrays.asList(
//                 CustomerCreateUpdateDTO.AddressCreateDTO.builder()
//                     .line1("123 Main Street")
//                     .cityId(1L)
//                     .countryId(1L)
//                     .build()
//             ))
//             .build();
        
//         when(customerService.createCustomer(any(CustomerCreateUpdateDTO.class)))
//             .thenReturn(customerDTO);
        
//         // Act & Assert
//         mockMvc.perform(post("/api/v1/customers")
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(objectMapper.writeValueAsString(createDTO)))
//             .andExpect(status().isCreated())
//             .andExpect(jsonPath("$.name").value("John Doe"));
//     }
    
//     @Test
//     @DisplayName("Should return 400 for invalid customer data")
//     void testCreateCustomerInvalidData() throws Exception {
//         // Arrange
//         String invalidJson = "{\"name\": \"\"}";
        
//         // Act & Assert
//         mockMvc.perform(post("/api/v1/customers")
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(invalidJson))
//             .andExpect(status().isBadRequest());
//     }
    
//     @Test
//     @DisplayName("Should update customer with status 200")
//     void testUpdateCustomer() throws Exception {
//         // Arrange
//         CustomerCreateUpdateDTO updateDTO = CustomerCreateUpdateDTO.builder()
//             .name("John Doe Updated")
//             .dateOfBirth(LocalDate.of(1990, 5, 15))
//             .nic("991234567V")
//             .mobileNumbers(Arrays.asList("0771234567"))
//             .addresses(Arrays.asList(
//                 CustomerCreateUpdateDTO.AddressCreateDTO.builder()
//                     .line1("456 New Street")
//                     .cityId(1L)
//                     .countryId(1L)
//                     .build()
//             ))
//             .build();
        
//         CustomerDTO updatedDTO = CustomerDTO.builder()
//             .id(1L)
//             .name("John Doe Updated")
//             .dateOfBirth(LocalDate.of(1990, 5, 15))
//             .nic("991234567V")
//             .mobileNumbers(Arrays.asList("0771234567"))
//             .build();
        
//         when(customerService.updateCustomer(eq(1L), any(CustomerCreateUpdateDTO.class)))
//             .thenReturn(updatedDTO);
        
//         // Act & Assert
//         mockMvc.perform(put("/api/v1/customers/1")
//             .contentType(MediaType.APPLICATION_JSON)
//             .content(objectMapper.writeValueAsString(updateDTO)))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.name").value("John Doe Updated"));
//     }
    
//     @Test
//     @DisplayName("Should delete customer with status 204")
//     void testDeleteCustomer() throws Exception {
//         // Arrange
//         doNothing().when(customerService).deleteCustomer(1L);
        
//         // Act & Assert
//         mockMvc.perform(delete("/api/v1/customers/1")
//             .contentType(MediaType.APPLICATION_JSON))
//             .andExpect(status().isNoContent());
        
//         verify(customerService, times(1)).deleteCustomer(1L);
//     }
    
//     @Test
//     @DisplayName("Should add mobile number successfully")
//     void testAddMobileNumber() throws Exception {
//         // Arrange
//         when(customerService.addMobileNumber(1L, "0770000000"))
//             .thenReturn(customerDTO);
        
//         // Act & Assert
//         mockMvc.perform(post("/api/v1/customers/1/mobile-numbers?mobileNumber=0770000000")
//             .contentType(MediaType.APPLICATION_JSON))
//             .andExpect(status().isOk());
//     }
    
//     @Test
//     @DisplayName("Should remove mobile number successfully")
//     void testRemoveMobileNumber() throws Exception {
//         // Arrange
//         when(customerService.removeMobileNumber(1L, "0771234567"))
//             .thenReturn(customerDTO);
        
//         // Act & Assert
//         mockMvc.perform(delete("/api/v1/customers/1/mobile-numbers?mobileNumber=0771234567")
//             .contentType(MediaType.APPLICATION_JSON))
//             .andExpect(status().isOk());
//     }
// }