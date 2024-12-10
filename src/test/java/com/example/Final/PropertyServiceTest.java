package com.example.Final;



import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.securityservice.User;
import com.example.Final.repository.ContactRepo;
import com.example.Final.repository.PropertyRepo;
import com.example.Final.service.PropertyService;
import com.example.Final.service.RentalHistoryService;
import com.example.Final.service.SalesHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PropertyServiceTest {

    @Mock
    private PropertyRepo propertyRepo;

    @Mock
    private ContactRepo contactRepo;

    @Mock
    private RentalHistoryService rentalHistoryService;

    @Mock
    private SalesHistoryService salesHistoryService;

    @InjectMocks
    private PropertyService propertyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        // Arrange
        Properties property = new Properties();
        when(propertyRepo.findAllByOrderByPropertyPriorityAsc()).thenReturn(Collections.singletonList(property));

        // Act
        List<Properties> result = propertyService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(property, result.get(0));
        verify(propertyRepo, times(1)).findAllByOrderByPropertyPriorityAsc();
    }

    @Test
    void testGetAllByUser() {
        // Arrange
        User user = new User();
        Properties property = new Properties();
        when(propertyRepo.getPropertiesByUser(user)).thenReturn(Collections.singletonList(property));

        // Act
        List<Properties> result = propertyService.getAllByUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(property, result.get(0));
        verify(propertyRepo, times(1)).getPropertiesByUser(user);
    }

    @Test
    void testCreate() {
        // Arrange
        Properties property = new Properties();
        when(propertyRepo.save(property)).thenReturn(property);

        // Act
        Properties result = propertyService.create(property);

        // Assert
        assertNotNull(result);
        assertEquals(property, result);
        verify(propertyRepo, times(1)).save(property);
    }

    @Test
    void testSave() {
        // Arrange
        Properties property = new Properties();

        // Act
        propertyService.save(property);

        // Assert
        verify(propertyRepo, times(1)).save(property);
    }

    @Test
    void testDeleteById() {
        // Arrange
        Properties property = new Properties();
        property.setPropertyId(1);
        when(propertyRepo.findById(1)).thenReturn(Optional.of(property));

        // Act
        propertyService.deleteById(1);

        // Assert
        assertFalse(property.isAvailable());
        verify(propertyRepo, times(1)).save(property);
    }

    @Test
    void testUpdateInfo() {
        // Arrange
        Properties property = new Properties();
        when(propertyRepo.findById(1)).thenReturn(Optional.of(property));

        // Act
        propertyService.updateInfo(1, "Nhà riêng", "Hợp pháp", "Hiện đại", 100.0, 2000000, 2, 3, 2);

        // Assert
        assertEquals("Nhà riêng", property.getPropertyType());
        assertEquals("Hợp pháp", property.getPropertyLegal());
        assertEquals("Hiện đại", property.getPropertyInterior());
        assertEquals(100.0, property.getSquareMeters());
        assertEquals(2000000, property.getPropertyPrice());
        assertEquals(2, property.getPropertyFloor());
        assertEquals(3, property.getBedrooms());
        assertEquals(2, property.getBathrooms());
        verify(propertyRepo, times(1)).save(property);
    }

    @Test
    void testSortPriceByAllASC() {
        // Arrange
        Properties property = new Properties();
        when(propertyRepo.getPropertiesByOrderByPropertyPriceAscPropertyPriorityAsc())
                .thenReturn(Collections.singletonList(property));

        // Act
        List<Properties> result = propertyService.sortPriceByAllASC();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(propertyRepo, times(1)).getPropertiesByOrderByPropertyPriceAscPropertyPriorityAsc();
    }

    @Test
    void testFindPropertiesByProvince() {
        // Arrange
        Properties property = new Properties();
        when(propertyRepo.findByAddress_Province("Hà Nội")).thenReturn(Collections.singletonList(property));

        // Act
        List<Properties> result = propertyService.findPropertiesByProvince("Hà Nội");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(propertyRepo, times(1)).findByAddress_Province("Hà Nội");
    }
}
