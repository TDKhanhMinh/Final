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
@Table(name = "sales_history")
public class SalesHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_history_id")
    private int saleListingId;

    @Column(name = "price")
    private double price;

    @Column(name = "status")
    private String status;

    @Column(name = "create_date")
    private String createDate;

    @Column(name = "source")
    private String source;

    @ManyToOne
    @JoinColumn(name = "properties_id")
    private Properties properties;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User buyer;


}

