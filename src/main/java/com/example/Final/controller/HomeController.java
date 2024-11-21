package com.example.Final.controller;

import com.example.Final.entity.listingservice.Properties;
import com.example.Final.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final PropertyService propertyService;

    @GetMapping("/home")
    public String getHome(Model model) {
        List<Properties> result = propertyService.getAll();
        Collections.shuffle(result);
        List<Properties> random = result.stream()
                .limit(8)
                .toList();
        model.addAttribute("randomProperty",random);
        return "home/homebody";
    }

    @GetMapping("/all-listings")
    public String getAllListings(Model model) {
        List<Properties> propertiesList = propertyService.getAll();
        model.addAttribute("properties", propertiesList);
        return "listing/all-listing";
    }


    @GetMapping("/post-info")
    public String getPostInfo() {
        return "listing/post-information";
    }

    @GetMapping("/post-contact")
    public String getPostContact() {
        return "listing/post-description-contact";
    }

    @GetMapping("/post-image")
    public String getPostImage() {
        return "listing/post-image";
    }

    @GetMapping("/access-denied")
    public String getAccessDenied() {
        return "user/login";
    }
}
