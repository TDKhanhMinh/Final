package com.example.Final.controller;

import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.securityservice.User;
import com.example.Final.service.HistoryListingService;
import com.example.Final.service.PropertyService;
import com.example.Final.service.UserService;
import org.springframework.aop.config.AopNamespaceHandler;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final HistoryListingService historyListingService;
    private final PropertyService propertyService;
    private final LocalValidatorFactoryBean defaultValidator;

    public AdminController(UserService userService, HistoryListingService historyListingService, PropertyService propertyService, LocalValidatorFactoryBean defaultValidator) {
        this.userService = userService;
        this.historyListingService = historyListingService;
        this.propertyService = propertyService;
        this.defaultValidator = defaultValidator;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "/admin/dashboard";
    }

    @GetMapping("/users")
    public String user(Model model) {

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
}
