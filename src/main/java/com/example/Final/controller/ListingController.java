package com.example.Final.controller;

import com.example.Final.entity.listingservice.*;
import com.example.Final.repository.AddressRepo;
import com.example.Final.repository.ContactRepo;
import com.example.Final.repository.ImagesRepo;
import com.example.Final.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/listing")
@RequiredArgsConstructor
public class ListingController {

    private final AddressRepo addressRepo;
    private final PropertyService propertyService;
    private final UserService userService;
    private final ContactRepo contactRepo;
    private final ImageUploadService imageUploadService;
    private final ImagesRepo imagesRepo;
    private final RentalHistoryService rentalHistoryService;
    private final SalesHistoryService salesHistoryService;
    private final HistoryListingService historyListingService;


    @GetMapping("/post-address")
    public String getPost(Model model) {
        model.addAttribute("address", new Address());
        return "listing/post-address";
    }

    @GetMapping("/post-info/")
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
            return "redirect:/home/home";
        }
        return "redirect:/home/home";
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
            // model.addAttribute("propertyNew", properties);
            return "listing/post-information";
        } else {
            redirectAttributes.addFlashAttribute("error", "Hãy điền tất cả thông tin");
            return "redirect:/listing/post-address";
        }

    }

    @PostMapping("/information")
    public String listProperties(Model model,
                                 @RequestParam("propertyId") int propertyId,
                                 @RequestParam("property-type") String type,
                                 @RequestParam("paper") String legal,
                                 @RequestParam("interior") String interior,
                                 @RequestParam("square-meters") double squareMeters,
                                 @RequestParam("price") double price,
                                 @RequestParam("floors") int floatFloors,
                                 @RequestParam("bedroom") int bedrooms,
                                 @RequestParam("bathroom") int bathrooms,
                                 Principal principal) {

        propertyService.updateInfo(propertyId, type, legal, interior, squareMeters, price, floatFloors, bedrooms, bathrooms);
        if (propertyService.getById(propertyId).getPropertyType().equals("rent")) {
            rentalHistoryService.createRentalHistory(propertyService.getById(propertyId));
        } else {
            salesHistoryService.createSalesHistory(propertyService.getById(propertyId));
        }

        model.addAttribute("properties", propertyService.getById(propertyId));
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

        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = currentDate.plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);
        contact.setDatePost(formattedDate);
        contact.setDateEnd(formattedEndDate);


        properties.setUser(userService.findUserByEmail(principal.getName()));
        properties.setContact(contactRepo.save(contact));
        propertyService.save(properties);
        model.addAttribute("property", properties);
        return "listing/post-image";
    }

    @PostMapping("/complete")
    public String complete(Model model,
                           @RequestParam("images") ArrayList<MultipartFile> images,
                           @RequestParam("propertyId") int propertyId) throws Exception {
        Properties properties = propertyService.getById(propertyId);
        List<Images> imageList = new ArrayList<>();
        for (MultipartFile file : images) {
            String path = imageUploadService.uploadImage(file);
            Images image = new Images();
            image.setImageUrl(path);
            image.setProperty(properties);
            imagesRepo.save(image);
            imageList.add(image);
        }
        properties.setListImages(imageList);
        propertyService.updateImages(properties);

        return "redirect:/home/home ";
    }

    @GetMapping("/listing-info/{id}")
    public String getListingInfo(@PathVariable int id, Model model,
                                 Principal principal) {
        Properties property = propertyService.getById(id);
        if (property.getHistoryListing() == null) {
            HistoryListing historyListing = historyListingService.createHistoryListing(property, userService.findUserByEmail(principal.getName()));
            property.setHistoryListing(historyListing);
            propertyService.save(property);
        }

        List<Properties> result = propertyService.getAll();
        Collections.shuffle(result);
        List<Properties> random = result.stream()
                .limit(8)
                .toList();
        model.addAttribute("randomProperty",random);
        model.addAttribute("property", property);
        model.addAttribute("historyListing", propertyService.getByHistoryListing(historyListingService.getByUser(userService.findUserByEmail(principal.getName()))));
        //model.addAttribute("historyListing", historyListingService);
        return "listing/listing-info";
    }

}
