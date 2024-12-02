package com.example.Final.service;

import com.example.Final.entity.paymentservice.UserPayment;
import com.example.Final.entity.paymentservice.UserPaymentDetails;
import com.example.Final.entity.securityservice.User;
import com.example.Final.repository.UserPaymentDetailsRepo;
import com.example.Final.repository.UserPaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserPaymentService {
    @Autowired
    private UserPaymentDetailsRepo userPaymentDetailsRepo;
    @Autowired
    private UserPaymentRepo userPaymentRepo;

    public void createUserPayment(User user, double amount, String paymentMethod) {

        UserPayment userPayment = user.getUserPayment();
        List<UserPaymentDetails> detailsList = userPayment.getPaymentDetails();

        UserPaymentDetails paymentDetails = new UserPaymentDetails();

        paymentDetails.setPaymentMethod(paymentMethod);
        paymentDetails.setAmount(amount-amount*0.1);
        paymentDetails.setStatus("Chờ duyệt");

        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);
        paymentDetails.setPayment(userPayment);
        paymentDetails.setDate(formattedDate);
        userPaymentDetailsRepo.save(paymentDetails);


        detailsList.add(paymentDetails);
        userPayment.setPaymentDetails(detailsList);

        userPaymentRepo.save(userPayment);

    }
}
