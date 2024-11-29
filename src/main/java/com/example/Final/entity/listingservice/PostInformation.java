package com.example.Final.entity.listingservice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contact")
public class PostInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private int contactId;
    private String fullName;
    private String email;
    private String phone;


    private String datePost;
    private String dateEnd;
    private String typePost;
    private int daysRemaining;
    private double payment;

    @OneToOne
    @JoinColumn(name = "contact_property_id")
    private Properties properties;

}

