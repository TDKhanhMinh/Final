package com.example.Final.repository;

import com.example.Final.entity.paymentservice.UserPaymentDetails;
import com.example.Final.entity.securityservice.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPaymentDetailsRepo extends JpaRepository<UserPaymentDetails, Integer> {

}
