package com.example.Final.controller;

import com.example.Final.entity.listingservice.Images;
import com.example.Final.entity.securityservice.User;
import com.example.Final.repository.ImagesRepo;
import com.example.Final.service.ImageUploadService;
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

    @PostMapping("/update-info")
    public String updateInfo(Model model,
                             @Param("images") ArrayList<MultipartFile> images,
                             @Param("fullName") String fullName,
                             @Param("email") String email,
                             @Param("phone") String phone,
                             Principal principal
    ) throws Exception {
        User user = userService.findUserByEmail(principal.getName());
        if (!images.isEmpty()) {
            List<Images> imageList = new ArrayList<>();
            for (MultipartFile file : images) {
                String path = imageUploadService.uploadImage(file);
                Images image = new Images();
                image.setImageUrl(path);
                image.setUser(user);
//                sửa chỗ này
                imagesRepo.save(image);
                userService.updateImage(user, image);
                imageList.add(image);
            }
        }
        userService.updateInfo(email, fullName, phone);
        return "redirect:/user/user-info";
    }

    @GetMapping("/changePassword")
    public String changePassword(Model model, Principal principal) {
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
}
