package com.example.Final.controller;

import com.example.Final.entity.listingservice.PostInformation;
import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.paymentservice.Payment;
import com.example.Final.entity.securityservice.User;
import com.example.Final.repository.ContactRepo;
import com.example.Final.service.PaymentService;
import com.example.Final.service.PropertyService;
import com.example.Final.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {


    private final PropertyService propertyService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final ContactRepo contactRepo;

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
                postInformation.setPayPerDay(postPrice);
            }
            case "VIP Bạc" -> {
                postInformation.setTypePost(adType);
                postPrice = 40000;
                postInformation.setPayPerDay(postPrice);
            }
            case "VIP Vàng" -> {
                postInformation.setTypePost(adType);
                postPrice = 100000;
                postInformation.setPayPerDay(postPrice);
            }
            default -> {

                postInformation.setTypePost(adType);
                postPrice = 2000;
                postInformation.setPayPerDay(postPrice);
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

        if (user.getAccountBalance() < payment) {
            paymentService.savePaymentFailure(payment, formattedDate, properties);
            model.addAttribute("error");
            return "redirect:/user/listing-manager-draft";
//            sửa cái này
        } else {
            paymentService.savePaymentSuccess(payment, formattedDate, properties);
            user.setAccountBalance(user.getAccountBalance() - payment);
            properties.setAvailable(true);
            propertyService.save(properties);
            userService.save(user);
            return "redirect:/user/listing-manager";
        }
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
    public String getPayment(Model model, RedirectAttributes redirectAttributes, @RequestParam(value = "propertyId") int propertyId,
                             @RequestParam(value = "userId") int userId) {
        User user = userService.findUserById(userId);
        Properties property = propertyService.getById(propertyId);
        Payment payment = property.getPayment();

        if (user.getAccountBalance() < property.getPostInformation().getPayment()) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản hiện không đủ tiền vui lòng nạp tiền thêm.");
            return "redirect:/payment/payment/" + userId;
        } else {
            user.setAccountBalance(user.getAccountBalance() - property.getPostInformation().getPayment());
            userService.save(user);
            payment.setStatus("Đã thanh toán");
            paymentService.save(payment);
            return "user/listing-manager";
        }

    }

    @GetMapping("/deposit")
    public String getDeposit(Model model, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "user/deposit-money";
    }
}
