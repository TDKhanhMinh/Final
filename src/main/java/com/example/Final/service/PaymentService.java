package com.example.Final.service;

import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.paymentservice.Payment;
import com.example.Final.repository.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepo paymentRepo;

    public void savePayment(double payment_amount, String dayPost, Properties properties) {
        if (properties.getPayment() == null) {
            Payment payment = new Payment();
            payment.setAmount(payment_amount);
            payment.setDate(dayPost);
            payment.setStatus("Đã thanh toán");
            payment.setProperties(properties);
            payment.setPaymentMethod("Thanh toán online");
            paymentRepo.save(payment);
        } else {
            Payment payment = properties.getPayment();
            payment.setAmount(payment_amount);
            payment.setDate(dayPost);
            payment.setStatus("Đã thanh toán");
            payment.setProperties(properties);
            payment.setPaymentMethod("Thanh toán online");
            paymentRepo.save(payment);
        }

    }
}
