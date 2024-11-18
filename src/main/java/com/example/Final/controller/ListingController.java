package com.example.Final.controller;

import com.example.Final.entity.listingservice.Address;
import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.securityservice.User;
import com.example.Final.repository.AddressRepo;
import com.example.Final.repository.PropertyRepo;
import com.example.Final.service.PropertyService;
import com.example.Final.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/listing")
@RequiredArgsConstructor
public class ListingController {

    private final AddressRepo addressRepo;
    private final PropertyRepo propertyRepo;
    private final PropertyService propertyService;
    private final UserService userService;

    @GetMapping("/post-address")
    public String getPost(Model model) {
        model.addAttribute("address", new Address());
        return "/listing/post-address";
    }

    @GetMapping("/post-info")
    public String getPostInfo(Model model) {
        return "/listing/post-information";
    }

    @GetMapping("/exit-post/{id}")
    public String getExitPost(@PathVariable("id") int id) {

        if (propertyRepo.findById(id).isPresent()) {
            propertyRepo.deleteById(id);
        } else if (addressRepo.findById(id).isPresent()) {
            addressRepo.deleteById(id);
        } else {
            return "home/homebody";
        }
        return "home/homebody";
    }

    @PostMapping("/address")
    public String listProperties(Model model, Principal principal,
                                 @RequestParam("location") String location,
                                 @RequestParam("option") String option,
                                 @ModelAttribute("address") Address address) {
        address.setStreet(location);
        Properties properties = new Properties();
        address.setProperties(properties);
        properties.setAddress(address);
        properties.setPropertyTypeTransaction(option);
      //  properties.setUser(userService.findUserByEmail(principal.getName()));
        propertyService.create(properties);

        model.addAttribute("address", address);
        model.addAttribute("propertyOld", properties);
        model.addAttribute("propertyNew", properties);
        return "/listing/post-information";
    }

    @PostMapping("/information")
    public String listProperties(Model model, Principal principal,
                                 @ModelAttribute("propertyOld") Properties properties,
                                 @RequestParam("property-type") String type,
                                 @RequestParam("interior") String interior,
                                 @RequestParam("property-old-id") int id) {
        properties.setPropertyType(type);
        properties.setPropertyInterior(interior);
        propertyService.create(properties);
        Properties newProperties = propertyRepo.findById(id).orElseThrow();
        propertyService.updateInfo(newProperties, properties);
        propertyService.deleteById(properties.getPropertyId());
       // User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("properties", newProperties);
        //model.addAttribute("user", user);
        return "/listing/post-description-contact";
    }

    @PostMapping("/contact")
    public String getContact() {
        //làm tiếp phần này
        return "/listing/post-image";
    }

}
