package com.example.Final.controller;

import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.paymentservice.Payment;
import com.example.Final.entity.paymentservice.UserPayment;
import com.example.Final.entity.securityservice.User;
import com.example.Final.repository.UserPaymentDetailsRepo;
import com.example.Final.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.aop.config.AopNamespaceHandler;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final HistoryListingService historyListingService;
    private final PropertyService propertyService;
    private final LocalValidatorFactoryBean defaultValidator;
    private final PaymentService paymentService;
    private final UserPaymentService userPaymentService;
    private final UserPaymentDetailsService userPaymentDetailsService;

    public AdminController(UserService userService, HistoryListingService historyListingService, PropertyService propertyService, LocalValidatorFactoryBean defaultValidator, PaymentService paymentService1, UserPaymentService userPaymentService, UserPaymentDetailsRepo userPaymentDetailsRepo, UserPaymentDetailsService userPaymentDetailsService) {
        this.userService = userService;
        this.historyListingService = historyListingService;
        this.propertyService = propertyService;
        this.defaultValidator = defaultValidator;
        this.paymentService = paymentService1;
        this.userPaymentService = userPaymentService;
        this.userPaymentDetailsService = userPaymentDetailsService;
    }
    @GetMapping("/logout")
    public String logout(HttpSession session, Model model) {
        model.addAttribute("success", "Log out successfully");
        session.removeAttribute("USERNAME");
        session.invalidate();
        System.out.println("User logged out");
        return "user/login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "/admin/dashboard";
    }

    @GetMapping("/users")
    public String user(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("users", userService.getAll());
        return "/admin/user";
    }

    @GetMapping("/listings")
    public String listing(Model model,
                          @RequestParam(name = "option", defaultValue = "0")int option) {
        List<Properties> propertyList = propertyService.getAll();
        model.addAttribute("properties", propertyService.caseGetAll(option));
        return "admin/listing";
    }
    @PostMapping("/checked")
    public String listingChecked(Model model, @RequestParam("id") int id) {
        Properties property = propertyService.getById(id);
        property.setPropertyStatus("Đã duyệt");
        property.setAvailable(true);
        propertyService.save(property);
        model.addAttribute("properties", propertyService.caseGetAll(1));
        return "/admin/listing";
    }
    @PostMapping("/denied")
    public String listingDenied(Model model, @RequestParam("id") int id) {
        Properties property = propertyService.getById(id);
        User author = userService.findUserById(property.getUser().getUserId());
        double cost = 0;
        Optional<Payment> optionalPayment = paymentService.getPaymentByPropertiesId(id);
        if(optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            cost = payment.getAmount();
        }
        author.setAccountBalance(author.getAccountBalance()+cost);
        property.setPropertyStatus("Từ chối");
        propertyService.save(property);
        model.addAttribute("properties", propertyService.caseGetAll(1));
        return "/admin/listing";
    }
    @PostMapping("/deleteListing")
    public String deleteListing(Model model, @RequestParam("id") int id) {
        Properties property = propertyService.getById(id);
        propertyService.delete(property);
        model.addAttribute("properties", propertyService.caseGetAll(1));
        model.addAttribute("notification", "Listing deleted successfully");
        return "/admin/listing";
    }
    @PostMapping("deleteUser")
    public String deleteUser(Model model, @RequestParam("id") int id) {
        for (Properties property : propertyService.getAll()) {
            if (property.getUser().getUserId() == id) {
                propertyService.delete(property);
            }
        }
        for (UserPayment payment : userPaymentService.getAll()){
            if (payment.getUser().getUserId() == id){
                paymentService.deletePayment(payment.getPaymentId());
                userPaymentService.deleteUserPayment(payment);

            }
        }

        userService.deleteById(id);
        model.addAttribute("users", userService.getAll());
        model.addAttribute("user", new User());
        model.addAttribute("notification", "User deleted successfully");
        return "/admin/user";
    }
    @PostMapping("/register")
    public String getRegisterForm(@Valid
                                  @ModelAttribute("user") User user,
                                  BindingResult result,
                                  Model model) {
        model.addAttribute("users", userService.getAll());
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("notification", "User registration failed");
            return "admin/user";
        }
        if (userService.findUserByEmail(user.getEmail()) != null) {
            model.addAttribute("user", user);
            model.addAttribute("notification", "Email has already been registered");
            return "admin/user";
        }
        if (user.getPassword().equals(user.getConfirmPassword())) {
            userService.create(user);
            model.addAttribute("notification", "Registration successfully");
            return "admin/user";
        } else {
            model.addAttribute("notification", "Password is not same");
            return "admin/user";
        }
    }

    @GetMapping("/payments")
    public String payment(Model model,
                          @RequestParam(name = "option", defaultValue = "0") int option ) {
        switch (option) {
            case 0:
                model.addAttribute("payments", paymentService.getAllPayments());
                model.addAttribute("userPayments", userPaymentDetailsService.getAll());
                return "admin/payment";
            case 1:
                model.addAttribute("userPayments", userPaymentDetailsService.getAll());
                model.addAttribute("payments", new ArrayList<Payment>());

                return "admin/payment";
            case 2:
                model.addAttribute("payments", paymentService.getAllPayments());
                model.addAttribute("userPayments", new ArrayList<UserPayment>());
                return "admin/payment";
            default:
                model.addAttribute("payments", new ArrayList<Payment>());
                model.addAttribute("userPayments", new ArrayList<UserPayment>());
                return "admin/payment";
        }
    }
}
