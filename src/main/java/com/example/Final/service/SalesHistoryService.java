package com.example.Final.service;

import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.salesservice.SalesHistory;
import com.example.Final.repository.SalesHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class SalesHistoryService {
    @Autowired
    private SalesHistoryRepo salesHistoryRepo;

    public void createSalesHistory(Properties properties) {
        SalesHistory salesHistory = new SalesHistory();
        salesHistory.setPrice(properties.getPropertyPrice());

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);

        salesHistory.setCreateDate(formattedDate);
        salesHistory.setProperties(properties);
        salesHistory.setStatus("Bán");
        salesHistory.setSource("Người môi giới cung cấp");

        salesHistoryRepo.save(salesHistory);
    }
}
