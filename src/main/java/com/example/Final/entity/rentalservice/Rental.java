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
@Table(name = "rental_listing")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_listing_id")
    private int rentalListingId;

    @Column(name = "rental_price")
    private double rentalPrice;
    @Column(name = "lease_start_date")
    private Date leaseStartDate;

    @Column(name = "lease_end_date")
    private Date leaseEndDate;
    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "properties_id")
    private Properties properties;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    private List<RentalPayment> payments;
}

