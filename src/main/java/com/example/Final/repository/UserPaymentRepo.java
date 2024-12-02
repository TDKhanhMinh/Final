package com.example.Final.repository;

import com.example.Final.entity.paymentservice.UserPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPaymentRepo extends JpaRepository<UserPayment, Integer> {
}
