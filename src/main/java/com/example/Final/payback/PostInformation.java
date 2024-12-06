package com.example.Final.payback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostInformation {
    private String fullName;
    private String email;
    private String phone;


    private String datePost;
    private String dateEnd;
    private String typePost;
    private int daysRemaining;
    private double payment;
    private double payPerDay;

    private Properties properties;

}

