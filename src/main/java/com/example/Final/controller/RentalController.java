package com.example.Final.controller;

import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.securityservice.User;
import com.example.Final.service.PropertyService;
import com.example.Final.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/rental")
@RequiredArgsConstructor
public class RentalController {
    private final UserService userService;
    private final PropertyService propertyService;

    @GetMapping("/all-rental")
    public String getListingRental(Model model, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("user", user);
        List<Properties> propertiesList = propertyService.getAllByUser(user);
        propertiesList.removeIf(properties -> !properties.isAvailable());
        propertiesList.removeIf(properties -> !properties.getPropertyTypeTransaction().equals("rent"));
        model.addAttribute("properties", propertiesList);
        model.addAttribute("city","Toàn quốc");
        return "listing/all-listing";
    }
}
