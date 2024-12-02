package com.example.Final.entity.paymentservice;

import com.example.Final.entity.securityservice.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_payemnt_details")
public class UserPaymentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int paymentId;
    @Column(name = "payment_amount")
    private double amount;
    @Column(name = "payment_date")
    private String date;
    @Column(name = "status")
    private String status;
    @Column(name = "payment_method")
    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "payment_user_id")
    private UserPayment payment;

}
