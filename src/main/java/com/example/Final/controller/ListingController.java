package com.example.Final.controller;

import com.example.Final.entity.listingservice.*;
import com.example.Final.entity.securityservice.User;
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
    private final PaymentService paymentService;


    @GetMapping("/post-address")
    public String getPost(Model model) {

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
    public String listProperties(Model model, RedirectAttributes redirectAttributes,
                                 @Param("location") String location,
                                 @RequestParam("option") String option,
                                 @RequestParam("city") String city,
                                 @RequestParam("district") String district,
                                 @RequestParam("ward") String ward,
                                 @RequestParam("address") String addressInput) {
        Address address = new Address();

        if (!location.trim().isEmpty()) {
            address.setStreet(location);
            address.setFullAddress(location + " " + ward + " " + district + " " + city);
        }
        if (!addressInput.trim().isEmpty()) {
            String[] list = addressInput.split(",");
            if (list.length == 5) {
                String tmp = list[0] + "," + list[1];
                address.setStreet(tmp);
            } else if (list.length > 5) {
                String tmp = list[0] + "," + list[1] + "," + list[2];
                address.setStreet(tmp);
            } else {
                address.setStreet(addressInput.split(",")[0]);
            }
            address.setFullAddress(addressInput);
        }

        address.setProvince(city.replace(",", "").replace("Tỉnh ", "").replace("Thành phố ", ""));
        address.setDistrict(district.replace(",", "").replace("Huyện ", "").replace("Thị xã ", ""));
        address.setWard(ward.replace(",", "").replace("Xã ", ""));

        Properties properties = new Properties();
        address.setProperties(properties);
        properties.setAddress(address);
        properties.setPropertyTypeTransaction(option);
        propertyService.create(properties);


        model.addAttribute("address", address);
        model.addAttribute("propertyOld", properties);
        // model.addAttribute("propertyNew", properties);
        return "listing/post-information";
    }

    @PostMapping("/information")
    public String listProperties(Model model, @RequestParam("propertyId") int propertyId, @RequestParam("property-type") String type, @RequestParam("paper") String legal, @RequestParam("interior") String interior, @RequestParam("square-meters") double squareMeters, @RequestParam("price") double price, @RequestParam("floors") int floatFloors, @RequestParam("bedroom") int bedrooms, @RequestParam("bathroom") int bathrooms, Principal principal) {

        propertyService.updateInfo(propertyId, type, legal, interior, squareMeters, price, floatFloors, bedrooms, bathrooms);
        if (propertyService.getById(propertyId).getPropertyTypeTransaction().equals("rent")) {
            rentalHistoryService.createRentalHistory(propertyService.getById(propertyId));

        } else {
            salesHistoryService.createSalesHistory(propertyService.getById(propertyId));
        }

        model.addAttribute("properties", propertyService.getById(propertyId));
        model.addAttribute("userName", principal.getName());
        model.addAttribute("contact", new PostInformation());
        return "listing/post-description-contact";
    }

    @PostMapping("/contact")
    public String getContact(@RequestParam("propertyId") int id, @RequestParam("title") String title, @RequestParam("description") String description, @ModelAttribute("contact") PostInformation postInformation, Model model, Principal principal) {
        Properties properties = propertyService.getById(id);
        properties.setPropertyDescription(description);
        properties.setPropertyTitle(title);
        postInformation.setProperties(properties);

        LocalDate currentDate = LocalDate.now();
//        LocalDate endDate = currentDate.plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);
//        String formattedEndDate = endDate.format(formatter);
        postInformation.setDatePost(formattedDate);
//        contact.setDateEnd(formattedEndDate);
        postInformation.setDaysRemaining(0);

        properties.setUser(userService.findUserByEmail(principal.getName()));
        properties.setPostInformation(contactRepo.save(postInformation));
        properties.setAvailable(true);
        propertyService.save(properties);
        model.addAttribute("property", properties);
        return "listing/post-image";
    }

    @PostMapping("/complete")
    public String complete(Model model, @RequestParam("images") ArrayList<MultipartFile> images,
                           @RequestParam("propertyId") int propertyId,
                           Principal principal) throws Exception {
        Properties properties = propertyService.getById(propertyId);
        User user = userService.findUserByEmail(principal.getName());

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

        model.addAttribute("property", properties);
        model.addAttribute("user", user);
        return "listing/post-payment";
    }

    @GetMapping("/listing-info/{id}")
    public String getListingInfo(@PathVariable int id, Model model, Principal principal) {
        Properties property = propertyService.getById(id);
        if (property.getHistoryListing() == null) {
            HistoryListing historyListing = historyListingService.createHistoryListing(property, userService.findUserByEmail(principal.getName()));
            property.setHistoryListing(historyListing);
            propertyService.save(property);
        }

        List<Properties> result = propertyService.getAll();
        Collections.shuffle(result);
        List<Properties> random = result.stream().limit(8).toList();
        List<Properties> history = propertyService.getByHistoryListing(historyListingService.getByUser(userService.findUserByEmail(principal.getName())));
        model.addAttribute("randomProperty", random);
        model.addAttribute("property", property);
        if (!history.isEmpty()) {
            model.addAttribute("historyListing", history);
        }

        model.addAttribute("historySize", history.size());
        return "listing/listing-info";
    }


    @PostMapping("/payment-post")
    public String postPaymentPost(Model model,
                                  @RequestParam("propertyId") int propertyId,
                                  @RequestParam("userId") int userId,
                                  @RequestParam("ad-type") String adType,
                                  @RequestParam("option-day") int optionDay) {
        User user = userService.findUserById(userId);

        Properties properties = propertyService.getById(propertyId);

        PostInformation postInformation = properties.getPostInformation();

        double postPrice;
        switch (adType) {
            case "VIP Kim Cương" -> {
                postInformation.setTypePost(adType);
                postPrice = 200000;
            }
            case "VIP Bạc" -> {
                postInformation.setTypePost(adType);
                postPrice = 40000;
            }
            case "VIP Vàng" -> {
                postInformation.setTypePost(adType);
                postPrice = 100000;
            }
            default -> {

                postInformation.setTypePost(adType);
                postPrice = 2000;
            }
        }
        double payment = postPrice * optionDay;
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = currentDate.plusDays(optionDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);

        postInformation.setDatePost(formattedDate);
        postInformation.setDateEnd(formattedEndDate);
        postInformation.setProperties(properties);
        postInformation.setPayment(payment);
        postInformation.setDaysRemaining(optionDay);

        properties.setPostInformation(contactRepo.save(postInformation));

        propertyService.save(properties);

        paymentService.savePayment(payment, formattedDate, properties);
        user.setAccountBalance(user.getAccountBalance() - payment);

        userService.save(user);
        return "redirect:/user/listing-manager";
    }

    @PostMapping("/test")
    public String test(@RequestParam("address") String addressInput) {
        System.out.println(addressInput);
        return "redirect:/user/listing-manager";
    }
}
