package com.example.Final.payback;

import com.example.Final.entity.listingservice.HistoryListing;
import com.example.Final.entity.paymentservice.Payment;
import com.example.Final.entity.rentalservice.RentalHistory;
import com.example.Final.entity.salesservice.SalesHistory;
import com.example.Final.entity.securityservice.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Properties {

    private String propertyTitle;
    private double propertyPrice;
    private String propertyType;
    private String propertyTypeTransaction;
    private boolean isAvailable;
    private String propertyInterior;
    private String propertyLegal;
    private String propertyDescription;
    private int propertyFloor;
    private String propertyStatus;
    private int bedrooms;
    private int bathrooms;
    private double squareMeters;
    private int propertyPriority;
    private Double propertyLongitude;
    private Double propertyLatitude;

    private User user;

    private List<Images> listImages;

    private Address address;

    private PostInformation postInformation;

    private List<RentalHistory> rentalHistories;

    private List<SalesHistory> salesHistories;

    private HistoryListing historyListing;

    private Payment payment;
}

