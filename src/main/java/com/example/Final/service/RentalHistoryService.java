package com.example.Final.service;

import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.rentalservice.RentalHistory;

import com.example.Final.repository.RentalHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Service
public class RentalHistoryService {

    @Autowired
    private RentalHistoryRepo rentalHistoryRepo;

    public void createRentalHistory(Properties properties) {
        RentalHistory rentalHistory = new RentalHistory();
        rentalHistory.setRentalPrice(properties.getPropertyPrice());

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);

        rentalHistory.setCreatedAt(formattedDate);
        rentalHistory.setProperties(properties);
        rentalHistory.setStatus("Cho thuê");
        rentalHistory.setSource("Người môi giới cung cấp");

        rentalHistoryRepo.save(rentalHistory);
    }




}
