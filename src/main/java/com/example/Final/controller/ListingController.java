package com.example.Final.controller;

import com.example.Final.payback.Properties;
import com.example.Final.payback.Address;
import com.example.Final.payback.PostInformation;
import com.example.Final.payback.Images;
import com.example.Final.entity.listingservice.*;
import com.example.Final.entity.securityservice.User;
import com.example.Final.payback.paybackService;
import com.example.Final.repository.AddressRepo;
import com.example.Final.repository.ContactRepo;
import com.example.Final.repository.ImagesRepo;
import com.example.Final.service.*;
import jakarta.servlet.http.HttpSession;
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
@SessionAttributes({"property", "address", "contact", "images"})
@RequestMapping("/listing")
@RequiredArgsConstructor
public class ListingController {

    private final AddressRepo addressRepo;
    private final PropertyService propertyService;
    private final UserService userService;
    private final ContactRepo contactRepo;
    private final ImageUploadService imageUploadService;
    private final ImagesRepo imagesRepo;
    private final HistoryListingService historyListingService;
    private final paybackService paybackService;



    @GetMapping("/post-address")
    public String getPost(Model model, HttpSession session) {
        if (session.getAttribute("USERNAME") == null) {
            model.addAttribute("error", "Hãy đăng nhập để đăng tin");
            return "user/login";
        } else {
            return "listing/post-address";
        }
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
    public String listProperties(Model model, HttpSession session,
                                 @RequestParam("location") String location,
                                 @RequestParam("option") String option,
                                 @RequestParam("city") String city,
                                 @RequestParam("district") String district,
                                 @RequestParam("ward") String ward,
                                 @RequestParam("address") String addressInput,
                                 @RequestParam("longitude") String longitude,
                                 @RequestParam("latitude") String latitude) {
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

        properties.setPropertyLatitude(Double.parseDouble(latitude));
        properties.setPropertyLongitude(Double.parseDouble(longitude));

        session.setAttribute("address", address);
        session.setAttribute("propertyOld", properties);
        return "listing/post-information";
    }


    @PostMapping("/information")
    public String listProperties(Model model, HttpSession session,
                                 @RequestParam("property-type") String type,
                                 @RequestParam("paper") String legal,
                                 @RequestParam("interior") String interior,
                                 @RequestParam("square-meters") double squareMeters,
                                 @RequestParam("price") double price,
                                 @RequestParam("floors") int floatFloors,
                                 @RequestParam("bedroom") int bedrooms,
                                 @RequestParam("bathroom") int bathrooms,
                                 Principal principal) {
        Properties properties = (Properties) session.getAttribute("propertyOld");
        Address address = (Address) session.getAttribute("address");
        paybackService.updateProperty(properties, type, legal, interior, squareMeters, price, floatFloors, bedrooms, bathrooms);

        User user = userService.findUserByEmail(principal.getName());

        session.setAttribute("properties", properties);
        session.setAttribute("address", address);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("contact", new PostInformation());

        return "listing/post-description-contact";
    }


    @PostMapping("/contact")
    public String getContact(HttpSession session,
                             @RequestParam("title") String title,
                             @RequestParam("description") String description,
                             @ModelAttribute("contact") PostInformation postInformation,
                             Model model, Principal principal) {

        Properties properties = (Properties) session.getAttribute("propertyOld");
        Address address = (Address) session.getAttribute("address");
        properties.setPropertyDescription(description);
        properties.setPropertyTitle(title);
        postInformation.setProperties(properties);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);
        postInformation.setDatePost(formattedDate);
        postInformation.setDaysRemaining(0);

        properties.setUser(userService.findUserByEmail(principal.getName()));
        properties.setPostInformation(postInformation);
        properties.setPropertyStatus("Chưa thanh toán");

        session.setAttribute("property", properties);
        session.setAttribute("address", address);
        session.setAttribute("contact", postInformation);
        return "listing/post-image";
    }


    @PostMapping("/complete")
    public String complete(Model model, HttpSession session,
                           @RequestParam("images") ArrayList<MultipartFile> images,
                           Principal principal) throws Exception {

        User user = userService.findUserByEmail(principal.getName());
        session.removeAttribute("images");
        session.setAttribute("images", new ArrayList<>());
        Properties properties = (Properties) session.getAttribute("propertyOld");
        Address address = (Address) session.getAttribute("address");
        PostInformation postInformation = (PostInformation) session.getAttribute("contact");
        List<Images> imageList = new ArrayList<>();
        for (MultipartFile file : images) {
            String path = imageUploadService.uploadImage(file);
            Images image = new Images();
            image.setImageUrl(path);
            image.setProperty(properties);
            imageList.add(image);
        }
        properties.setListImages(imageList);
        session.setAttribute("property", properties);
        session.setAttribute("address", address);
        session.setAttribute("contact", postInformation);
        session.setAttribute("images", imageList);
        model.addAttribute("user", user);
        return "listing/post-payment";
    }


    @GetMapping("/listing-info/{id}")
    public String getListingInfo(@PathVariable int id, Model model,
                                 Principal principal, HttpSession session) {
        if (session.getAttribute("USERNAME") == null) {
            model.addAttribute("error", "Hãy đăng nhập để xem thông tin");
            return "user/login";
        } else {
            com.example.Final.entity.listingservice.Properties property = propertyService.getById(id);
            if (property.getHistoryListing() == null) {
                HistoryListing historyListing = historyListingService.createHistoryListing(property, userService.findUserByEmail(principal.getName()));
                property.setHistoryListing(historyListing);
                propertyService.save(property);
            }

            List<com.example.Final.entity.listingservice.Properties> result = propertyService.getAll();
            Collections.shuffle(result);
            ArrayList<com.example.Final.entity.listingservice.Properties> random = new ArrayList<>(result.stream().limit(8).toList());
            List<com.example.Final.entity.listingservice.Properties> history = propertyService.getByHistoryListing(historyListingService.getByUser(userService.findUserByEmail(principal.getName())));
            random.removeIf(properties -> !properties.isAvailable());
            model.addAttribute("randomProperty", random);
            model.addAttribute("property", property);
            if (!history.isEmpty()) {
                model.addAttribute("historyListing", history);
            }
            model.addAttribute("historySize", history.size());
            return "listing/listing-info";
        }

    }


}
