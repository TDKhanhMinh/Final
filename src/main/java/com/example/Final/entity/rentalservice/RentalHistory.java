package com.example.Final.entity.rentalservice;

import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.securityservice.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rental_history")
public class RentalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_history_id")
    private int rentalListingId;

    @Column(name = "price")
    private double rentalPrice;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "source")
    private String source;


    @ManyToOne
    @JoinColumn(name = "properties_id")
    private Properties properties;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}

