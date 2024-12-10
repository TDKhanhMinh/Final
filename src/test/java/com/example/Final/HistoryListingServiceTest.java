package com.example.Final;


import com.example.Final.entity.listingservice.HistoryListing;
import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.securityservice.User;
import com.example.Final.repository.HistoryListingRepo;
import com.example.Final.service.HistoryListingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoryListingServiceTest {

    @Mock
    private HistoryListingRepo historyListingRepo;

    @InjectMocks
    private HistoryListingService historyListingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateHistoryListing_NewHistory() {
        // Arrange
        Properties property = new Properties();
        User user = new User();
        user.setHistoryListing(null);
        HistoryListing savedHistory = new HistoryListing();
        savedHistory.setPropertiesList(Collections.singletonList(property));
        savedHistory.setUser(user);

        when(historyListingRepo.save(any(HistoryListing.class))).thenReturn(savedHistory);

        // Act
        HistoryListing result = historyListingService.createHistoryListing(property, user);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getPropertiesList().size());
        assertEquals(user, result.getUser());
        verify(historyListingRepo, times(1)).save(any(HistoryListing.class));
    }



    @Test
    void testGetById_Found() {
        // Arrange
        int id = 1;
        HistoryListing historyListing = new HistoryListing();
        when(historyListingRepo.findById(id)).thenReturn(Optional.of(historyListing));

        // Act
        HistoryListing result = historyListingService.getById(id);

        // Assert
        assertNotNull(result);
        assertEquals(historyListing, result);
        verify(historyListingRepo, times(1)).findById(id);
    }

    @Test
    void testGetById_NotFound() {
        // Arrange
        int id = 1;
        when(historyListingRepo.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> historyListingService.getById(id));
        assertEquals("Couldn't find history'", exception.getMessage());
        verify(historyListingRepo, times(1)).findById(id);
    }

    @Test
    void testGetByUser() {
        // Arrange
        User user = new User();
        HistoryListing historyListing = new HistoryListing();
        when(historyListingRepo.findHistoryListingByUser(user)).thenReturn(historyListing);

        // Act
        HistoryListing result = historyListingService.getByUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(historyListing, result);
        verify(historyListingRepo, times(1)).findHistoryListingByUser(user);
    }
}
