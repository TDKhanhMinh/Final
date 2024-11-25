package com.example.Final.entity.listingservice;

import com.example.Final.entity.securityservice.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "history_listing")
public class HistoryListing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_listing_id")
    private int historyId;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "historyListing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Properties> propertiesList;
}
