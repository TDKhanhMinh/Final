package com.example.Final.entity.listingservice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
public class Images {

    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imageId;

    @Column(name = "image_url")
    private String imageUrl;


    @ManyToOne(cascade = CascadeType.ALL
            , fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Properties property;


}

