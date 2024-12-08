package com.example.Final.controller;

import com.example.Final.config.TestSecurityConfig;
import com.example.Final.entity.listingservice.Address;
import com.example.Final.entity.listingservice.Images;
import com.example.Final.entity.listingservice.PostInformation;
import com.example.Final.entity.listingservice.Properties;
import com.example.Final.entity.securityservice.User;
import com.example.Final.repository.ContactRepo;
import com.example.Final.repository.PropertyRepo;
import com.example.Final.repository.UserPaymentDetailsRepo;
import com.example.Final.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Import(TestSecurityConfig.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest {

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
    @Mock
    private TaskExecutionProperties taskExecutionProperties;
    @Autowired
    private HomeController homeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        homeController = new HomeController(propertyService, taskExecutionProperties);
    }

    @Test
    public void testGetHome() throws Exception {
        Properties property1 = new Properties();
        property1.setAvailable(true);
        Properties property2 = new Properties();
        property2.setAvailable(true);

        List<Properties> propertiesList = Arrays.asList(property1, property2);

        when(propertyService.getAll()).thenReturn(propertiesList);

        mockMvc.perform(get("/home/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/homebody"))
                .andExpect(model().attributeExists("randomProperty"));
    }

    @Test
    public void testGetAllListings() throws Exception {
        List<Images> imagesList = new ArrayList<>();
        Images image1 = new Images();
        Images image2 = new Images();
        Images image3 = new Images();
        image1.setImageUrl("abc");
        image2.setImageUrl("def");
        image3.setImageUrl("ghi");
        imagesList.add(image1);
        imagesList.add(image2);
        imagesList.add(image3);

        Address address = new Address();
        address.setFullAddress("somewhere in the earth");

        Properties property1 = new Properties();
        property1.setAvailable(true);
        PostInformation postInformation1 = new PostInformation();
        postInformation1.setTypePost("VIP Kim Cương");
        property1.setPostInformation(postInformation1);
        property1.setListImages(imagesList);
        property1.setAddress(address);
        property1.setPropertyTypeTransaction("rent");
        property1.setPropertyPrice(1000000000);
        property1.setSquareMeters(120);

        Properties property2 = new Properties();
        property2.setAvailable(true);
        PostInformation postInformation2 = new PostInformation();
        postInformation2.setTypePost("VIP Vàng");
        property2.setPostInformation(postInformation2);
        property2.setListImages(imagesList);
        property2.setAddress(address);
        property2.setPropertyTypeTransaction("sell");
        property2.setPropertyPrice(900000000);
        property2.setSquareMeters(120);

        List<Properties> propertiesList = Arrays.asList(property1, property2);

        when(propertyService.getAll()).thenReturn(propertiesList);

        mockMvc.perform(get("/home/all-listings"))
                .andExpect(status().isOk())
                .andExpect(view().name("listing/all-listing"))
                .andExpect(model().attributeExists("properties"))
                .andExpect(model().attribute("city", "Toàn quốc"));
    }
    @Test
    public void testGetPostInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/home/post-info"))
                .andExpect(MockMvcResultMatchers.status().isOk())  // Status 200 OK
                .andExpect(MockMvcResultMatchers.view().name("listing/post-information"));
    }

}
