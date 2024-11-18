package com.example.Final.entity.salesservice;

import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.securityservice.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sales_transaction")
public class SalesTransactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transactionId;

    @Column(name = "price")
    private double price;

    @Column(name = "status")
    private String status;

    @Column(name = "trans_date")
    private Date transactionDate;

    @ManyToOne
    @JoinColumn(name = "properties_id")
    private Properties properties;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User buyer;


}

