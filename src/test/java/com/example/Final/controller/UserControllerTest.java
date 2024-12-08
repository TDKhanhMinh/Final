package com.example.Final.controller;

import com.example.Final.config.TestSecurityConfig;
import com.example.Final.entity.securityservice.User;
import com.example.Final.repository.AddressRepo;
import com.example.Final.repository.ImagesRepo;
import com.example.Final.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.security.Principal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PropertyService propertyService;

    @MockBean
    private ImageUploadService imageUploadService;

    @MockBean
    private AddressRepo addressRepo;

    @MockBean
    private ImagesRepo imagesRepo;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    public void setUp() {

        User mockUser = new User();
        mockUser.setFullName("Test User");
        mockUser.setEmail("test@example.com");
        when(userService.findUserByEmail(anyString())).thenReturn(mockUser);

    }

    @Test
    public void testGetRegister() throws Exception {
        mockMvc.perform(get("/user/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testGetLogin() throws Exception {
        mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }

    @Test
    public void testGetChangePassword() throws Exception {
        mockMvc.perform(get("/user/changePassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/change-password"));
    }

    @Test
    public void testForgotPassword() throws Exception {
        mockMvc.perform(get("/user/forgotPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/forgot-password"));
    }

}
