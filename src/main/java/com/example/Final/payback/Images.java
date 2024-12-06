package com.example.Final.payback;

import com.example.Final.entity.securityservice.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Images {

    private String imageUrl;
    private Properties property;
    private User user;

}

