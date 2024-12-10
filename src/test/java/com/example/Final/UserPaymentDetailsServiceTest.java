package com.example.Final;



import com.example.Final.entity.paymentservice.UserPaymentDetails;
import com.example.Final.repository.UserPaymentDetailsRepo;
import com.example.Final.service.UserPaymentDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserPaymentDetailsServiceTest {

    @Mock
    private UserPaymentDetailsRepo userPaymentDetailsRepo;

    @InjectMocks
    private UserPaymentDetailsService userPaymentDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        // Arrange
        UserPaymentDetails detail1 = new UserPaymentDetails();
        detail1.setPaymentId(1);
        detail1.setPaymentMethod("Credit Card");

        UserPaymentDetails detail2 = new UserPaymentDetails();
        detail2.setPaymentId(2);
        detail2.setPaymentMethod("PayPal");

        List<UserPaymentDetails> mockDetails = Arrays.asList(detail1, detail2);

        when(userPaymentDetailsRepo.findAll()).thenReturn(mockDetails);

        // Act
        List<UserPaymentDetails> result = userPaymentDetailsService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Credit Card", result.get(0).getPaymentMethod());
        assertEquals("PayPal", result.get(1).getPaymentMethod());

        verify(userPaymentDetailsRepo, times(1)).findAll();
    }
}
