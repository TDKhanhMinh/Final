package com.example.Final.service;

import com.example.Final.entity.paymentservice.UserPaymentDetails;
import com.example.Final.repository.UserPaymentDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPaymentDetailsService {
    @Autowired
    private UserPaymentDetailsRepo userPaymentDetailsRepo;

    public List<UserPaymentDetails> getAll() {
        return userPaymentDetailsRepo.findAll();
    }
}
