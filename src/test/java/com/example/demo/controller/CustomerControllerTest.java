package com.example.demo.controller;

import com.example.demo.dto.CustomerCreateUpdateDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@DisplayName("Customer Controller Tests")
class CustomerControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CustomerService customerService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private CustomerDTO customerDTO;
    
    @BeforeEach
    void setUp() {
        customerDTO = CustomerDTO.builder()
            .id(1L)
            .name("John Doe")
            .dateOfBirth(LocalDate.of(1990, 5, 15))
            .nic("991234567V")
            .mobileNumbers(Arrays.asList("0771234567", "0772345678"))
            .addresses(Arrays.asList())
            .familyMembers(Arrays.asList())
            .build();
    }
    
    @Test
    @DisplayName("Should return customer by ID with status 200")
    void testGetCustomerById() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(customerDTO);
        
        mockMvc.perform(get("/api/v1/customers/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.nic").value("991234567V"));
        
        verify(customerService, times(1)).getCustomerById(1L);
    }
    
    @Test
    @DisplayName("Should return 404 when customer not found")
    void testGetCustomerByIdNotFound() throws Exception {
        when(customerService.getCustomerById(999L))
            .thenThrow(new com.example.demo.exception.CustomerNotFoundException(
                "Customer not found with ID: 999", 999L));
        
        mockMvc.perform(get("/api/v1/customers/999")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("Should return all customers with status 200")
    void testGetAllCustomers() throws Exception {
        List<CustomerDTO> customers = Arrays.asList(customerDTO);
        when(customerService.getAllCustomers()).thenReturn(customers);
        
        mockMvc.perform(get("/api/v1/customers")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name").value("John Doe"));
        
        verify(customerService, times(1)).getAllCustomers();
    }
    
    @Test
    @DisplayName("Should get customer by NIC successfully")
    void testGetCustomerByNic() throws Exception {
        when(customerService.getCustomerByNic("991234567V")).thenReturn(customerDTO);
        
        mockMvc.perform(get("/api/v1/customers/nic/991234567V")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nic").value("991234567V"));
    }
    
    @Test
    @DisplayName("Should search customers by name")
    void testSearchByName() throws Exception {
        List<CustomerDTO> customers = Arrays.asList(customerDTO);
        when(customerService.searchByName("John")).thenReturn(customers);
        
        mockMvc.perform(get("/api/v1/customers/search/by-name?name=John")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }
    
    @Test
    @DisplayName("Should create customer with status 201")
    void testCreateCustomer() throws Exception {
        CustomerCreateUpdateDTO createDTO = CustomerCreateUpdateDTO.builder()
            .name("John Doe")
            .dateOfBirth(LocalDate.of(1990, 5, 15))
            .nic("991234567V")
            .mobileNumbers(Arrays.asList("0771234567"))
            .addresses(Arrays.asList(
                CustomerCreateUpdateDTO.AddressCreateDTO.builder()
                    .line1("123 Main Street")
                    .cityId(1L)
                    .countryId(1L)
                    .build()
            ))
            .build();
        
        when(customerService.createCustomer(any(CustomerCreateUpdateDTO.class)))
            .thenReturn(customerDTO);
        
        mockMvc.perform(post("/api/v1/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("John Doe"));
    }
    
    @Test
    @DisplayName("Should return 400 for invalid customer data")
    void testCreateCustomerInvalidData() throws Exception {
        String invalidJson = "{\"name\": \"\"}";
        
        mockMvc.perform(post("/api/v1/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidJson))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("Should update customer with status 200")
    void testUpdateCustomer() throws Exception {
        CustomerCreateUpdateDTO updateDTO = CustomerCreateUpdateDTO.builder()
            .name("John Doe Updated")
            .dateOfBirth(LocalDate.of(1990, 5, 15))
            .nic("991234567V")
            .mobileNumbers(Arrays.asList("0771234567"))
            .addresses(Arrays.asList(
                CustomerCreateUpdateDTO.AddressCreateDTO.builder()
                    .line1("456 New Street")
                    .cityId(1L)
                    .countryId(1L)
                    .build()
            ))
            .build();
        
        CustomerDTO updatedDTO = CustomerDTO.builder()
            .id(1L)
            .name("John Doe Updated")
            .dateOfBirth(LocalDate.of(1990, 5, 15))
            .nic("991234567V")
            .mobileNumbers(Arrays.asList("0771234567"))
            .build();
        
        when(customerService.updateCustomer(eq(1L), any(CustomerCreateUpdateDTO.class)))
            .thenReturn(updatedDTO);
        
        mockMvc.perform(put("/api/v1/customers/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John Doe Updated"));
    }
    
    @Test
    @DisplayName("Should delete customer with status 204")
    void testDeleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);
        
        mockMvc.perform(delete("/api/v1/customers/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        
        verify(customerService, times(1)).deleteCustomer(1L);
    }
    
    @Test
    @DisplayName("Should add mobile number successfully")
    void testAddMobileNumber() throws Exception {
        when(customerService.addMobileNumber(1L, "0770000000"))
            .thenReturn(customerDTO);
        
        mockMvc.perform(post("/api/v1/customers/1/mobile-numbers?mobileNumber=0770000000")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("Should remove mobile number successfully")
    void testRemoveMobileNumber() throws Exception {
        when(customerService.removeMobileNumber(1L, "0771234567"))
            .thenReturn(customerDTO);
        
        mockMvc.perform(delete("/api/v1/customers/1/mobile-numbers?mobileNumber=0771234567")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}