package com.example.Final.payback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Address {
    private String street;
    private String ward;
    private String district;
    private String province;
    private String fullAddress;
    private Properties properties;
}
