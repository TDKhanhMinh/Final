package com.example.Final.repository;

import com.example.Final.entity.listingservice.HistoryListing;
import com.example.Final.entity.listingservice.Properties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepo extends JpaRepository<Properties, Integer> {
    List<Properties> getPropertiesByHistoryListing(HistoryListing historyListing);
}
