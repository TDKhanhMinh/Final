package com.example.Final.controller;

import com.example.Final.entity.listingservice.Address;
import com.example.Final.entity.listingservice.Contact;
import com.example.Final.entity.listingservice.Images;
import com.example.Final.entity.listingservice.Properties;
import com.example.Final.repository.AddressRepo;
import com.example.Final.repository.ContactRepo;
import com.example.Final.service.PropertyService;
import com.example.Final.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/listing")
@RequiredArgsConstructor
public class ListingController {

    private final AddressRepo addressRepo;
    private final PropertyService propertyService;
    private final UserService userService;
    private final ContactRepo contactRepo;

    @GetMapping("/post-address")
    public String getPost(Model model) {
        model.addAttribute("address", new Address());
        return "listing/post-address";
    }

    @GetMapping("/post-info")
    public String getPostInfo(Model model) {
        return "listing/post-information";
    }

    @GetMapping("/exit-post/{id}")
    public String getExitPost(@PathVariable("id") int id) {

        if (propertyService.getById(id) != null) {
            propertyService.deleteById(id);
        } else if (addressRepo.findById(id).isPresent()) {
            addressRepo.deleteById(id);
        } else {
            return "home/homebody";
        }
        return "home/homebody";
    }

    @PostMapping("/address")
    public String listProperties(Model model,
                                 RedirectAttributes redirectAttributes,
                                 @Param("location") String location,
                                 @Param("option") String option,
                                 @ModelAttribute("address") Address address) {
        if (location != null && option != null && address != null && !location.trim().isEmpty()) {
            address.setStreet(location);
            Properties properties = new Properties();
            address.setProperties(properties);
            properties.setAddress(address);
            properties.setPropertyTypeTransaction(option);
            propertyService.create(properties);
            model.addAttribute("address", address);
            model.addAttribute("propertyOld", properties);
            model.addAttribute("propertyNew", properties);
            return "listing/post-information";
        } else {
            redirectAttributes.addFlashAttribute("error", "Hãy điền tất cả thông tin");
            return "redirect:/listing/post-address";
        }

    }

    @PostMapping("/information")
    public String listProperties(Model model,
                                 @ModelAttribute("propertyOld") Properties properties,
                                 @RequestParam("property-type") String type,
                                 @RequestParam("interior") String interior,
                                 @RequestParam("property-old-id") int id,
                                 Principal principal) {
        properties.setPropertyType(type);
        properties.setPropertyInterior(interior);
        propertyService.create(properties);
        Properties newProperties = propertyService.getById(id);
        propertyService.updateInfo(newProperties, properties);
        propertyService.deleteById(properties.getPropertyId());
        model.addAttribute("properties", newProperties);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("contact", new Contact());
        return "listing/post-description-contact";
    }

    @PostMapping("/contact")
    public String getContact(@RequestParam("propertyId") int id,
                             @RequestParam("title") String title,
                             @RequestParam("description") String description,
                             @ModelAttribute("contact") Contact contact,
                             Model model, Principal principal) {
        Properties properties = propertyService.getById(id);
        properties.setPropertyDescription(description);
        properties.setPropertyTitle(title);
        contact.setProperties(properties);
        properties.setUser(userService.findUserByEmail(principal.getName()));
        properties.setContact(contactRepo.save(contact));
        propertyService.save(properties);
        model.addAttribute("property", properties);
        return "listing/post-image";
    }

    @PostMapping("/complete")
    public String complete(Model model,
                           @RequestParam("images") ArrayList<MultipartFile> images,
                           Principal principal){
        List<Images> imageList =  new ArrayList<Images>();
        for(MultipartFile file: images){
            String path = imageUploadService.uploadImage(file);
            Images image = new Images();
            image.setPath(path);
            imageList.add(image);
        }
    }
    @GetMapping("/listing-info")
    public String getListingInfo() {
        return "listing/listing-info";
    }

}
