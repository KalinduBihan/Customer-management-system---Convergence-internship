package com.example.demo.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for Customer response data
 * Used when returning customer information to clients
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private String nic;
    private List<String> mobileNumbers;
    private List<AddressDTO> addresses;
    private List<CustomerDTO> familyMembers;
}