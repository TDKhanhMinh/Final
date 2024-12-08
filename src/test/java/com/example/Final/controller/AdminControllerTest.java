package com.example.Final.controller;

import com.example.Final.controller.AdminController;
import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.securityservice.User;
import com.example.Final.repository.ContactRepo;
import com.example.Final.repository.PropertyRepo;
import com.example.Final.repository.UserPaymentDetailsRepo;
import com.example.Final.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Import(PropertyService.class)
@WebMvcTest(controllers = AdminController.class)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private HistoryListingService historyListingService;
    @MockBean
    private PropertyRepo propertyRepo;
    @MockBean
    private ContactRepo contactRepo;
    @MockBean
    private RentalHistoryService rentalHistoryService;
    @MockBean
    private SalesHistoryService salesHistoryService;
    @MockBean
    private PaymentService paymentService;
    @MockBean
    private UserPaymentService userPaymentService;
    @MockBean
    private UserPaymentDetailsService userPaymentDetailsService;
    @MockBean
    private UserPaymentDetailsRepo userPaymentDetailsRepo;

    private AdminController adminController;
    @Autowired
    private LocalValidatorFactoryBean defaultValidator;
    @MockBean
    private PropertyService propertyService;
    @Mock
    private Model model;
    @Mock
    private BindingResult bindingResult;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminController = new AdminController(userService, historyListingService, propertyService, defaultValidator, paymentService, userPaymentService, userPaymentDetailsRepo, userPaymentDetailsService );
    }

    @Test
    void testDashboard_AsAdmin() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("admin", "password", List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
        String result = adminController.dashboard();
        assertEquals("/admin/dashboard", result);
    }

    @Test
    void testDeleteUser_AsAdmin() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("admin", "password", List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
        Model model = mock(Model.class);
        when(userService.getAll()).thenReturn(List.of(new User(), new User()));
        String result = adminController.deleteUser(model, 1);
        assertEquals("/admin/user", result);
        verify(model).addAttribute("notification", "User deleted successfully");
    }
    @Test
    void testDeleteListing_AsAdmin() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("admin", "password", List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));

        Model model = mock(Model.class);
        when(propertyService.getById(1)).thenReturn(new Properties());

        String result = adminController.deleteListing(model, 1);
        assertEquals("/admin/listing", result);
        verify(model).addAttribute("notification", "Listing deleted successfully");
    }
    @Test
    void testListingChecked() {
        int propertyId = 1;
        Properties property = new Properties();
        property.setPropertyId(propertyId);
        property.setPropertyStatus("Chưa duyệt");
        property.setAvailable(false);

        when(propertyService.getById(propertyId)).thenReturn(property);

        String result = adminController.listingChecked(model, propertyId);

        assertEquals("Đã duyệt", property.getPropertyStatus());

        verify(propertyService).save(property);

        verify(model).addAttribute(eq("properties"), anyList());

        assertEquals("/admin/listing", result);
    }
    @Test
    void testRegister_SuccessfulRegistration() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setConfirmPassword("password123");

        when(userService.findUserByEmail(user.getEmail())).thenReturn(null);

        when(bindingResult.hasErrors()).thenReturn(false);

        String result = adminController.getRegisterForm(user, bindingResult, model);

        verify(userService).create(user);

        verify(model).addAttribute("notification", "Registration successfully");

        assertEquals("admin/user", result);
    }

    @Test
    void testRegister_EmailAlreadyRegistered() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setConfirmPassword("password123");

        when(userService.findUserByEmail(user.getEmail())).thenReturn(new User());

        String result = adminController.getRegisterForm(user, bindingResult, model);

        verify(model).addAttribute("notification", "Email has already been registered");

        assertEquals("admin/user", result);
    }

    @Test
    void testRegister_PasswordMismatch() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setConfirmPassword("password456");

        when(bindingResult.hasErrors()).thenReturn(false);

        String result = adminController.getRegisterForm(user, bindingResult, model);

        verify(model).addAttribute("notification", "Password is not same");

        assertEquals("admin/user", result);
    }

    @Test
    void testRegister_ValidationError() {
        User user = new User();
        user.setPassword("password123");
        user.setConfirmPassword("password123");

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("user", "email", "Email is required")));

        String result = adminController.getRegisterForm(user, bindingResult, model);

        verify(userService, never()).create(user);

        verify(model).addAttribute("notification", "User registration failed");

        assertEquals("admin/user", result);
    }

}