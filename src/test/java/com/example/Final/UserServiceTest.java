package com.example.Final;



import com.example.Final.entity.listingservice.Images;
import com.example.Final.entity.paymentservice.UserPayment;
import com.example.Final.entity.securityservice.Roles;
import com.example.Final.entity.securityservice.User;
import com.example.Final.repository.RolesRepository;
import com.example.Final.repository.UserPaymentRepo;
import com.example.Final.repository.UserRepository;
import com.example.Final.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private UserPaymentRepo userPaymentRepo;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setConfirmPassword("password123");
        user.setFullName("John Doe");
        user.setPhone("123456789");

        Roles role = new Roles();
        role.setName("ROLE_REALTOR");

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(rolesRepository.findRolesByName("ROLE_REALTOR")).thenReturn(role);

        UserPayment userPayment = new UserPayment();
        when(userPaymentRepo.save(any(UserPayment.class))).thenReturn(userPayment);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User savedUser = userService.create(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("John Doe", savedUser.getFullName());
        assertEquals("123456789", savedUser.getPhone());
        assertNotNull(savedUser.getRoles());
        assertTrue(savedUser.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_REALTOR")));
        assertNotNull(savedUser.getUserPayment());
        verify(userRepository, times(1)).save(savedUser);
        verify(userPaymentRepo, times(2)).save(any(UserPayment.class));
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        Roles role = new Roles();
        role.setName("ROLE_USER");
        user.setRoles(Collections.singletonList(role));

        when(userRepository.findUserByEmail("test@example.com")).thenReturn(user);

        // Act
        org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User) userService.loadUserByUsername("test@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        when(userRepository.findUserByEmail("nonexistent@example.com")).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("nonexistent@example.com"));
    }
}
