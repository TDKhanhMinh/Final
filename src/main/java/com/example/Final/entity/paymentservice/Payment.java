package com.example.Final.entity.paymentservice;

import com.example.Final.entity.listingservice.Properties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment {
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

    @OneToOne
    @JoinColumn(name = "payment_property_id")
    private Properties properties;

}