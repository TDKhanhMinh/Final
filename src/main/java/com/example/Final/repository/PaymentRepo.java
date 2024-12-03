package com.example.Final.repository;

import com.example.Final.entity.paymentservice.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Integer> {
    Payment findByPropertiesPropertyId(int propertyId);
}
