package com.example.Final.entity.paymentservice;

import com.example.Final.entity.rentalservice.Rental;
import com.example.Final.entity.salesservice.SalesTransactions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "commission")
public class Commissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commission_id")
    private int commissionId;
    @Column(name = "rate")
    private double rate;
    @Column(name = "commission_amount")
    private double amount;
    @Column(name = "commission_status")
    private String status;
    @Column(name = "commission_created_at")
    private Date createdAt;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private SalesTransactions salesTransaction;

    @OneToOne
    @JoinColumn(name = "rental_listing_id")
    private Rental rentalTransaction;

    @OneToOne(mappedBy = "commission", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;


}