package com.example.Final.controller;

import com.example.Final.entity.listingservice.Address;
import com.example.Final.entity.listingservice.PostInformation;
import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.paymentservice.Payment;
import com.example.Final.entity.securityservice.User;
import com.example.Final.payback.Images;
import com.example.Final.repository.ContactRepo;
import com.example.Final.repository.ImagesRepo;
import com.example.Final.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {


    private final PropertyService propertyService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final ContactRepo contactRepo;
    private final RentalHistoryService rentalHistoryService;
    private final SalesHistoryService salesHistoryService;
    private final ImagesRepo imagesRepo;

    @PostMapping("/payment-post")
    public String postPaymentPost(Model model, HttpSession session,
                                  @RequestParam("userId") int userId,
                                  @RequestParam("ad-type") String adType,
                                  @RequestParam("option-day") int optionDay) {
        User user = userService.findUserById(userId);
        com.example.Final.payback.Properties prop = (com.example.Final.payback.Properties) session.getAttribute("propertyOld");
        com.example.Final.payback.Address addr = (com.example.Final.payback.Address) session.getAttribute("address");
        com.example.Final.payback.PostInformation pi = (com.example.Final.payback.PostInformation) session.getAttribute("contact");

        List<Images> images = (List<Images>) session.getAttribute("images");
        if (images == null) {
            images = new ArrayList<>();
        }

        Properties properties = new Properties();
        PostInformation postInformation = new PostInformation();
        Address address = new Address();
        List<com.example.Final.entity.listingservice.Images> imagesList = new ArrayList<>();
        address.setProvince(addr.getProvince());
        address.setDistrict(addr.getDistrict());
        address.setWard(addr.getWard());
        address.setStreet(addr.getStreet());
        address.setFullAddress(addr.getFullAddress());
        address.setProperties(properties);
        properties.setAddress(address);

        properties.setPropertyTypeTransaction(prop.getPropertyType());
        propertyService.create(properties);
        properties.setPropertyLatitude(prop.getPropertyLatitude());
        properties.setPropertyLongitude(prop.getPropertyLongitude());
        propertyService.updateInfo(properties.getPropertyId(),prop.getPropertyType(), prop.getPropertyLegal(), prop.getPropertyInterior(), prop.getSquareMeters(), prop.getPropertyPrice(), prop.getPropertyFloor(), prop.getBedrooms(), prop.getBathrooms());
        if (properties.getPropertyTypeTransaction().equals("rent")) {
            rentalHistoryService.createRentalHistory(properties);

        } else {
            salesHistoryService.createSalesHistory(properties);
        }
        properties.setPropertyDescription(prop.getPropertyDescription());
        properties.setPropertyTitle(prop.getPropertyTitle());
        postInformation.setProperties(properties);
        postInformation.setDatePost(pi.getDatePost());
        postInformation.setDaysRemaining(0);
        properties.setPostInformation(postInformation);
        properties.setPropertyStatus("Chưa thanh toán");
        for (Images img : images) {
            com.example.Final.entity.listingservice.Images image = new com.example.Final.entity.listingservice.Images();
            image.setImageUrl(img.getImageUrl());
            image.setProperty(properties);
            imagesRepo.save(image);
            imagesList.add(image);
        }
        properties.setListImages(imagesList);
        properties.setUser(user);
        propertyService.updateImages(properties);
        propertyService.save(properties);
        
        double postPrice;
        int priority;
        switch (adType) {
            case "VIP Kim Cương" -> {
                postInformation.setTypePost(adType);
                postPrice = 200000;
                postInformation.setPayPerDay(postPrice);
                priority = 1;
            }
            case "VIP Bạc" -> {
                postInformation.setTypePost(adType);
                postPrice = 40000;
                postInformation.setPayPerDay(postPrice);
                priority = 3;
            }
            case "VIP Vàng" -> {
                postInformation.setTypePost(adType);
                postPrice = 100000;
                postInformation.setPayPerDay(postPrice);
                priority = 2;
            }
            default -> {

                postInformation.setTypePost(adType);
                postPrice = 2000;
                postInformation.setPayPerDay(postPrice);
                priority = 4;
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
        properties.setPropertyPriority(priority);

        propertyService.save(properties);
        paymentService.savePayment(payment, formattedDate, properties);
        return "redirect:/payment/payment/" + properties.getPropertyId();
    }


    @GetMapping("/payment/{id}")
    public String getPayment(@PathVariable int id, Principal principal, Model model) {
        User user = userService.findUserByEmail(principal.getName());
        Properties property = propertyService.getById(id);
        model.addAttribute("property", property);
        model.addAttribute("user", user);
        return "user/payment";
    }

    @PostMapping("/user-payment")
    public String getPayment(Model model, RedirectAttributes redirectAttributes,
                             @RequestParam(value = "propertyId") int propertyId,
                             @RequestParam(value = "userId") int userId) {
        User user = userService.findUserById(userId);
        Properties property = propertyService.getById(propertyId);
        if (user.getAccountBalance() < property.getPostInformation().getPayment()) {
            paymentService.savePaymentFailure(property);
            redirectAttributes.addFlashAttribute("error", "Tài khoản hiện không đủ tiền vui lòng nạp tiền thêm.");
            return "redirect:/payment/payment/" + userId;
        } else {
            user.setAccountBalance(user.getAccountBalance() - property.getPostInformation().getPayment());
            paymentService.savePaymentSuccess(property);
            userService.save(user);
            redirectAttributes.addFlashAttribute("success", "Thanh toán bài đăng thành công");
            return "redirect:/user/listing-manager";
        }

    }

    @GetMapping("/deposit")
    public String getDeposit(Model model, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "user/deposit-money";
    }

    @PostMapping("/payment-option")
    public String getTransferVnPay(Model model, Principal principal,
                                   @RequestParam("paymentOption") String paymentOption) {
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("paymentOption", paymentOption);
        return "user/payment-option";
    }

    private final UserPaymentService userPaymentService;

    @PostMapping("/payment-method")
    public String getPaymentVnPay(Model model, Principal principal,
                                  @RequestParam("paymentMethod") String paymentMethod,
                                  @Param("money") String money,
                                  @Param("moneyValue") String moneyValue) {
        User user = userService.findUserByEmail(principal.getName());
        if (paymentMethod.equals("vnpay")) {
            double amount;
            if (moneyValue == null) {
                amount = Double.parseDouble(money);
            } else {
                amount = Double.parseDouble(moneyValue);
            }
            userPaymentService.createUserPayment(user, amount, paymentMethod);
            model.addAttribute("user", user);
            model.addAttribute("amount", amount);
            model.addAttribute("paymentOption", paymentMethod);
            return "user/payment-vnpay";
        } else {
            double amount;
            if (moneyValue == null) {
                amount = Double.parseDouble(money);
            } else {
                amount = Double.parseDouble(moneyValue);
            }
            userPaymentService.createUserPayment(user, amount, paymentMethod);
            model.addAttribute("amount", amount);
            model.addAttribute("user", user);
            model.addAttribute("paymentOption", paymentMethod);

            return "user/payment-momo";
        }
    }

}
