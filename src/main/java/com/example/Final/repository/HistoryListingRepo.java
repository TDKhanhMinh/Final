package com.example.Final.repository;

import com.example.Final.entity.listingservice.HistoryListing;
import com.example.Final.entity.securityservice.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryListingRepo extends JpaRepository<HistoryListing, Integer> {
    HistoryListing getHistoryListingByUser(User user);

    HistoryListing findHistoryListingByUser(User user);
}
