package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {
    
    private Long id;
    private String line1;
    private String line2;
    private CityDTO city;
    private CountryDTO country;
}