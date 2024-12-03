package com.example.Final.service;

import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.paymentservice.Payment;
import com.example.Final.repository.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private PropertyService propertyService;

    public void savePaymentSuccess(Properties properties) {
        Payment payment = properties.getPayment();
        payment.setStatus("Đã thanh toán");
        paymentRepo.save(payment);
        properties.setPropertyStatus("Chờ duyệt");
        propertyService.save(properties);
    }

    public void savePayment(double payment_amount, String dayPost, Properties properties) {
        Payment payment = new Payment();
        payment.setAmount(payment_amount);
        payment.setDate(dayPost);
        payment.setStatus("Chưa thanh toán");
        payment.setProperties(properties);
        payment.setPaymentMethod("Thanh toán online");
        paymentRepo.save(payment);

        properties.setPayment(payment);
        propertyService.save(properties);
    }

    public void savePaymentFailure(Properties properties) {
        Payment payment = properties.getPayment();
        payment.setStatus("Chưa thanh toán");
        paymentRepo.save(payment);


    }

    public void save(Payment payment) {
        paymentRepo.save(payment);
    }

    public Optional<Payment> getPaymentByPropertiesId(int id) {
        return Optional.ofNullable(paymentRepo.findByPropertiesPropertyId(id));
    }
    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }
    public void deletePayment(int id) {
        paymentRepo.deleteById(id);
    }
}
