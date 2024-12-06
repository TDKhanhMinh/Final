package com.example.Final.payback;

import com.example.Final.entity.securityservice.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryListing {

    private User user;
    private List<Properties> propertiesList;
}
