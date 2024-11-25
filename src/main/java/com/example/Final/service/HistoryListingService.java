package com.example.Final.service;

import com.example.Final.entity.listingservice.HistoryListing;
import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.securityservice.User;
import com.example.Final.repository.HistoryListingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class HistoryListingService {
    @Autowired
    private HistoryListingRepo historyListingRepo;

    public HistoryListing createHistoryListing(Properties properties, User user) {
        HistoryListing historyListing;
        if (user.getHistoryListing() == null) {
            historyListing = new HistoryListing();
            historyListing.setPropertiesList(Collections.singletonList(properties));
            historyListing.setUser(user);
        } else {
            historyListing = historyListingRepo.getHistoryListingByUser(user);
            List<Properties> list = historyListing.getPropertiesList();
            list.add(properties);
            historyListing.setPropertiesList(list);
        }
        return historyListingRepo.save(historyListing);

    }

    public HistoryListing getById(int id) {
        return historyListingRepo.findById(id).orElseThrow(() -> new RuntimeException("Couldn't find history'"));
    }

    public HistoryListing getByUser(User user) {
        return historyListingRepo.findHistoryListingByUser(user);
    }
}
