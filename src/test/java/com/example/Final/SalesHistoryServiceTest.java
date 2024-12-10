package com.example.Final;


import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.salesservice.SalesHistory;
import com.example.Final.repository.SalesHistoryRepo;
import com.example.Final.service.SalesHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SalesHistoryServiceTest {

    @Mock
    private SalesHistoryRepo salesHistoryRepo;

    @InjectMocks
    private SalesHistoryService salesHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSalesHistory() {
        // Arrange
        Properties properties = new Properties();
        properties.setPropertyPrice(5000000.0);

        // Act
        salesHistoryService.createSalesHistory(properties);

        // Assert
        ArgumentCaptor<SalesHistory> captor = ArgumentCaptor.forClass(SalesHistory.class);
        verify(salesHistoryRepo, times(1)).save(captor.capture());

        SalesHistory savedSalesHistory = captor.getValue();

        // Assertions
        assertEquals(5000000.0, savedSalesHistory.getPrice());
        assertEquals("Bán", savedSalesHistory.getStatus());
        assertEquals("Người môi giới cung cấp", savedSalesHistory.getSource());

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String expectedDate = currentDate.format(formatter);

        assertEquals(expectedDate, savedSalesHistory.getCreateDate());
        assertEquals(properties, savedSalesHistory.getProperties());
    }
}

