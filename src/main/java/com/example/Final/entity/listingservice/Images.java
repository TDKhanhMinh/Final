package com.example.Final.entity.listingservice;

import com.example.Final.entity.securityservice.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
@EqualsAndHashCode(exclude = "property")
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

    @OneToOne
    @JoinColumn(name = "image_user_id")
    private User user;

}

