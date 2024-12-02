package com.example.Final.controller;

import com.example.Final.entity.listingservice.Address;
import com.example.Final.entity.listingservice.Images;
import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.securityservice.User;
import com.example.Final.repository.AddressRepo;
import com.example.Final.repository.ImagesRepo;
import com.example.Final.service.ImageUploadService;
import com.example.Final.service.PropertyService;
import com.example.Final.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final BCryptPasswordEncoder encoder;
    private final ImageUploadService imageUploadService;
    private final ImagesRepo imagesRepo;
    private final PropertyService propertyService;
    private final AddressRepo addressRepo;

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        return "user/login";
    }

    @PostMapping("/register")
    public String getRegisterForm(@Valid
                                  @ModelAttribute("user") User user,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "user/register";
        }
        if (userService.findUserByEmail(user.getEmail()) != null) {
            model.addAttribute("user", user);
            model.addAttribute("error", "Email has already been registered");
            return "user/register";
        }
        if (user.getPassword().equals(user.getConfirmPassword())) {
            userService.create(user);
            model.addAttribute("success", "Registration successfully");
            return "user/login";
        } else {
            model.addAttribute("error", "Password is not same");
            return "user/register";
        }
    }

    @PostMapping("/update-image")
    public String updateImage(Model model, @Param("images") ArrayList<MultipartFile> images,
                              Principal principal) throws Exception {
        User user = userService.findUserByEmail(principal.getName());
        List<Images> imageList = new ArrayList<>();
        for (MultipartFile file : images) {
            String path = imageUploadService.uploadImage(file);
            Images image = new Images();
            image.setImageUrl(path);
            userService.updateImage(user, image);
            image.setUser(user);
            imagesRepo.save(image);
            imageList.add(image);
        }
        return "redirect:/user/user-info";
    }

    @PostMapping("/update-info")
    public String updateInfo(Model model,
                             @Param("fullName") String fullName,
                             @Param("email") String email,
                             @Param("phone") String phone) {
        userService.updateInfo(email, fullName, phone);
        return "redirect:/user/user-info";
    }

    @GetMapping("/changePassword")
    public String changePassword() {
        return "user/change-password";
    }

    @GetMapping("/cancelChange")
    public String cancelChange(Model model, Principal principal) {
        return "redirect:/user/user-info";
    }

    @PostMapping("/changePassword")
    public String changePasswordSave(Model model, Principal principal,
                                     @RequestParam("password") String password,
                                     @RequestParam("confirmPassword") String confirmPassword) {
        userService.updateByEmail(principal.getName(), password, confirmPassword);
        return "redirect:/home/home";
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "user/forgot-password";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@Valid RedirectAttributes redirectAttributes,
                                @RequestParam("email") String email,
                                @RequestParam("password") String password,
                                @RequestParam("confirmPassword") String confirmPassword) {
        if (email.isEmpty() || password.isEmpty() | confirmPassword.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "All fields are required");
            return "redirect:/user/forgotPassword";
        } else if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/user/forgotPassword";
        } else if (userService.findUserByEmail(email) == null) {
            redirectAttributes.addFlashAttribute("error", "Email does not exist");
            return "redirect:/user/forgotPassword";
        } else {
            userService.updateByEmail(email, password, confirmPassword);
            redirectAttributes.addFlashAttribute("success", "Password reset successfully");
            return "redirect:/user/login";
        }
    }

    @GetMapping("/user-info")
    public String getUserInfo(Model model, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "user/user-information";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        model.addAttribute("success", "Log out successfully");
        session.removeAttribute("USERNAME");
        session.invalidate();
        System.out.println("User logged out");
        return "user/login";
    }


    @GetMapping("/listing-manager")
    public String getListingManager(Model model, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("user", user);
        List<Properties> propertiesList = propertyService.getAllByUser(user);

//        chinhr cais nay
//        propertiesList.removeIf(properties -> !properties.isAvailable());
//        propertiesList.removeIf(properties -> properties.getPayment().getStatus().equals("Chưa thanh toán"));
        model.addAttribute("properties", propertiesList);
        return "user/listing-manager";
    }

    @GetMapping("/listing-manager-draft")
    public String getListingManagerDraft(Model model, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("user", user);
        List<Properties> propertiesList = propertyService.getAllByUser(user);
        propertiesList.removeIf(properties -> !properties.getPayment().getStatus().equals("Chưa thanh toán"));
        model.addAttribute("properties", propertiesList);
        return "user/draft-manager";
    }

    @GetMapping("/update-post/{id}")
    public String updatePost(Model model, Principal principal, @PathVariable int id) {
        User user = userService.findUserByEmail(principal.getName());
        List<Properties> propertiesList = propertyService.getAllByUser(user);
        model.addAttribute("user", user);
        for (Properties properties : propertiesList) {
            if (properties.getPropertyId() == id) {
                model.addAttribute("property", properties);
                return "user/update-post";
            }
        }
        return "user/update-post";
    }

    @PostMapping("/update-post")
    public String updatePost(@ModelAttribute("property") Properties properties,
                             @RequestParam("propertyId") int id,

                             @Param("city") String city,
                             @Param("district") String district,
                             @Param("ward") String ward,
                             @Param("location") String location,
                             @Param("property-type") String propertyType,
                             @Param("paper") String legal,
                             @Param("interior") String interior,
                             @Param("images") ArrayList<MultipartFile> images,
                             Principal principal,
                             Model model) throws Exception {
        Properties oldProperties = propertyService.getById(id);
        Address address = oldProperties.getAddress();

        if (propertyType != null) {
            oldProperties.setPropertyType(propertyType);
        }
        if (legal != null) {
            oldProperties.setPropertyLegal(legal);
        }
        if (interior != null) {
            oldProperties.setPropertyInterior(interior);
        }

        if (city != null) {
            address.setProvince(city);
            address.setDistrict(district);
            address.setWard(ward);
            address.setStreet(location);
            addressRepo.save(address);
        }

        oldProperties.setAddress(address);
        propertyService.save(oldProperties);


        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("property", oldProperties);
        model.addAttribute("user", user);
//        User user = userService.findUserByEmail(principal.getName());
//        model.addAttribute("property", properties);
//        model.addAttribute("user", user);


//        if (images != null) {
//            List<Images> imageList = oldProperties.getListImages();
//            for (MultipartFile file : images) {
//                String path = imageUploadService.uploadImage(file);
//                Images image = new Images();
//                image.setImageUrl(path);
//                image.setProperty(oldProperties);
//                imagesRepo.save(image);
//                imageList.add(image);
//            }
//            oldProperties.setListImages(imageList);
//            propertyService.save(oldProperties);
//        }

        propertyService.updateProperty(properties);

        return "redirect:/user/listing-manager";
    }


    @GetMapping("/delete-post/{id}")
    public String deletePost(@PathVariable int id) {
        propertyService.deleteById(id);
        return "redirect:/user/listing-manager";
    }

    @GetMapping("/payment-history")
    public String paymentHistory(Model model, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "user/payment-history";
    }


}
