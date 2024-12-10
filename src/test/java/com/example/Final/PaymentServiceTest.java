package com.example.Final;



import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.paymentservice.Payment;
import com.example.Final.repository.PaymentRepo;
import com.example.Final.service.PaymentService;
import com.example.Final.service.PropertyService;
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

class PaymentServiceTest {

    @Mock
    private PaymentRepo paymentRepo;

    @Mock
    private PropertyService propertyService;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSavePaymentSuccess() {
        // Arrange
        Properties properties = new Properties();
        Payment payment = new Payment();
        properties.setPayment(payment);

        when(paymentRepo.save(any(Payment.class))).thenReturn(payment);
        doNothing().when(propertyService).save(properties);

        // Act
        paymentService.savePaymentSuccess(properties);

        // Assert
        assertEquals("Đã thanh toán", payment.getStatus());
        assertEquals("Chờ duyệt", properties.getPropertyStatus());
        verify(paymentRepo, times(1)).save(payment);
        verify(propertyService, times(1)).save(properties);
    }


    @Test
    void testSavePaymentFailure() {
        // Arrange
        Properties properties = new Properties();
        Payment payment = new Payment();
        properties.setPayment(payment);

        when(paymentRepo.save(any(Payment.class))).thenReturn(payment);

        // Act
        paymentService.savePaymentFailure(properties);

        // Assert
        assertEquals("Chưa thanh toán", payment.getStatus());
        verify(paymentRepo, times(1)).save(payment);
    }

    @Test
    void testSave() {
        // Arrange
        Payment payment = new Payment();

        when(paymentRepo.save(payment)).thenReturn(payment);

        // Act
        paymentService.save(payment);

        // Assert
        verify(paymentRepo, times(1)).save(payment);
    }

    @Test
    void testGetPaymentByPropertiesId_Found() {
        // Arrange
        int propertyId = 1;
        Payment payment = new Payment();
        when(paymentRepo.findByPropertiesPropertyId(propertyId)).thenReturn(payment);

        // Act
        Optional<Payment> result = paymentService.getPaymentByPropertiesId(propertyId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(payment, result.get());
        verify(paymentRepo, times(1)).findByPropertiesPropertyId(propertyId);
    }

    @Test
    void testGetPaymentByPropertiesId_NotFound() {
        // Arrange
        int propertyId = 1;
        when(paymentRepo.findByPropertiesPropertyId(propertyId)).thenReturn(null);

        // Act
        Optional<Payment> result = paymentService.getPaymentByPropertiesId(propertyId);

        // Assert
        assertFalse(result.isPresent());
        verify(paymentRepo, times(1)).findByPropertiesPropertyId(propertyId);
    }

    @Test
    void testGetAllPayments() {
        // Arrange
        Payment payment = new Payment();
        when(paymentRepo.findAll()).thenReturn(Collections.singletonList(payment));

        // Act
        List<Payment> payments = paymentService.getAllPayments();

        // Assert
        assertNotNull(payments);
        assertEquals(1, payments.size());
        assertEquals(payment, payments.get(0));
        verify(paymentRepo, times(1)).findAll();
    }

    @Test
    void testDeletePayment() {
        // Arrange
        int paymentId = 1;
        doNothing().when(paymentRepo).deleteById(paymentId);

        // Act
        paymentService.deletePayment(paymentId);

        // Assert
        verify(paymentRepo, times(1)).deleteById(paymentId);
    }
}

