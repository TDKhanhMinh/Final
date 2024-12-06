package com.example.Final.entity.listingservice;

import com.example.Final.entity.paymentservice.Payment;
import com.example.Final.entity.rentalservice.RentalHistory;
import com.example.Final.entity.salesservice.SalesHistory;
import com.example.Final.entity.securityservice.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "properties")
public class Properties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private int propertyId;
    @Column(name = "property_title")
    private String propertyTitle;
    @Column(name = "property_price")
    private double propertyPrice;
    @Column(name = "property_type")
    private String propertyType;
    @Column(name = "property_type_transaction")
    private String propertyTypeTransaction;
    @Column(name = "property_is_available")
    private boolean isAvailable;
    @Column(name = "property_interior")
    private String propertyInterior;
    @Column(name = "property_legal_papers")
    private String propertyLegal;
    @Column(name = "property_description", length = Integer.MAX_VALUE)
    private String propertyDescription;
    @Column(name = "property_floor")
    private int propertyFloor;
    @Column(name = "property_status")
    private String propertyStatus;
    @Column(name = "property_bedrooms")
    private int bedrooms;
    @Column(name = "property_bathrooms")
    private int bathrooms;
    @Column(name = "property_square_meters")
    private double squareMeters;
    @Column(name = "property_priority")
    private int propertyPriority;
    @Column(name = "property_longitude")
    private Double propertyLongitude;
    @Column(name = "property_latitude")
    private Double propertyLatitude;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Images> listImages;

    @OneToOne(mappedBy = "properties", cascade = CascadeType.ALL)
    private Address address;

    @OneToOne(mappedBy = "properties", cascade = CascadeType.ALL)
    private PostInformation postInformation;

    @OneToMany(mappedBy = "properties", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RentalHistory> rentalHistories;

    @OneToMany(mappedBy = "properties", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SalesHistory> salesHistories;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "history_list_id")
    private HistoryListing historyListing;

    @OneToOne(mappedBy = "properties", cascade = CascadeType.ALL)
    private Payment payment;
}

