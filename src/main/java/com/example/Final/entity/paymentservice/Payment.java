package com.example.Final.entity.paymentservice;

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
    private int transactionId;
    @Column(name = "payment_amount")
    private int amount;
    @Column(name = "payment_date")
    private Date date;
    @Column(name = "status")
    private String status;
    @Column(name = "payment_method")
    private String paymentMethod;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "commission", referencedColumnName = "commission_id")
    private Commissions commission;

}