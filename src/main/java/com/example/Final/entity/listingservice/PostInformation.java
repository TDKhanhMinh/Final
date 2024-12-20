package com.example.Final.entity.listingservice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_information")
@EqualsAndHashCode(exclude = "properties")
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
    private double payPerDay;

    @OneToOne
    @JoinColumn(name = "post_property_id")
    private Properties properties;

}

