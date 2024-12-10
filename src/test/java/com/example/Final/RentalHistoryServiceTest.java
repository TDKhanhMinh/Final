package com.example.Final;



import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.rentalservice.RentalHistory;
import com.example.Final.repository.RentalHistoryRepo;
import com.example.Final.service.RentalHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RentalHistoryServiceTest {

    @Mock
    private RentalHistoryRepo rentalHistoryRepo;

    @InjectMocks
    private RentalHistoryService rentalHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRentalHistory() {
        // Arrange
        Properties properties = new Properties();
        properties.setPropertyPrice(1000.0);

        // Act
        rentalHistoryService.createRentalHistory(properties);

        // Assert
        verify(rentalHistoryRepo, times(1)).save(any(RentalHistory.class));
    }
}
