package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for creating and updating customers
 * Includes validation annotations for input validation
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerCreateUpdateDTO {
    
    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @NotNull(message = "Date of birth is required")
    @PastOrPresent(message = "Date of birth cannot be in the future")
    private LocalDate dateOfBirth;
    
    @NotBlank(message = "NIC is required")
    @Size(min = 10, max = 20, message = "NIC must be between 10 and 20 characters")
    @Pattern(regexp = "^[0-9]{9}[VvXx]?$", message = "Invalid NIC format")
    private String nic;
    
    @NotNull(message = "Mobile numbers list cannot be null")
    @Size(min = 1, message = "At least one mobile number is required")
    private List<@Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits") String> mobileNumbers;
    
    @NotNull(message = "Addresses list cannot be null")
    @Size(min = 1, message = "At least one address is required")
    private List<AddressCreateDTO> addresses;
    
    private List<Long> familyMemberIds;
    
    /**
     * Inner DTO for address creation
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddressCreateDTO {
        
        @NotBlank(message = "Address line 1 is required")
        @Size(min = 5, max = 255, message = "Address line 1 must be between 5 and 255 characters")
        private String line1;
        
        @Size(max = 255, message = "Address line 2 must not exceed 255 characters")
        private String line2;
        
        @NotNull(message = "City ID is required")
        private Long cityId;
        
        @NotNull(message = "Country ID is required")
        private Long countryId;
    }
}