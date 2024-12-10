package com.example.Final;



import com.example.Final.entity.paymentservice.UserPayment;
import com.example.Final.entity.paymentservice.UserPaymentDetails;
import com.example.Final.entity.securityservice.User;
import com.example.Final.repository.UserPaymentDetailsRepo;
import com.example.Final.repository.UserPaymentRepo;
import com.example.Final.service.UserPaymentService;
import com.example.Final.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserPaymentServiceTest {

    @Mock
    private UserPaymentDetailsRepo userPaymentDetailsRepo;

    @Mock
    private UserPaymentRepo userPaymentRepo;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserPaymentService userPaymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserPayment() {
        // Arrange
        User user = new User();
        user.setUserId(1);
        user.setAccountBalance(1000.0);

        UserPayment userPayment = new UserPayment();
        userPayment.setPaymentDetails(new ArrayList<>());
        user.setUserPayment(userPayment);

        double amount = 1000.0;
        String paymentMethod = "Credit Card";

        // Act
        userPaymentService.createUserPayment(user, amount, paymentMethod);

        // Assert
        verify(userPaymentDetailsRepo, times(1)).save(any(UserPaymentDetails.class));
        verify(userPaymentRepo, times(1)).save(userPayment);
        verify(userService, times(1)).save(user);

        // Kiểm tra rằng số dư tài khoản của user được cập nhật đúng
        assertEquals(1900.0, user.getAccountBalance(), 0.01); // 1000 + (1000 * 0.9)

        // Kiểm tra rằng danh sách payment details được cập nhật
        List<UserPaymentDetails> paymentDetailsList = userPayment.getPaymentDetails();
        assertEquals(1, paymentDetailsList.size());
        assertEquals("Credit Card", paymentDetailsList.get(0).getPaymentMethod());
        assertEquals(900.0, paymentDetailsList.get(0).getAmount(), 0.01); // 1000 - 10%
        assertEquals("Đã thanh toán", paymentDetailsList.get(0).getStatus());
    }
}

