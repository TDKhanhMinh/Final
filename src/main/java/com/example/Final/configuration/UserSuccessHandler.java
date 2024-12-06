package com.example.Final.configuration;

import com.example.Final.entity.securityservice.User;
import com.example.Final.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class UserSuccessHandler implements AuthenticationSuccessHandler, LogoutSuccessHandler {

    private final UserService userService;

    public UserSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("In customAuthenticationSuccessHandler");
        String userName = authentication.getName();
        System.out.println("userName=" + userName);

        User theUser = userService.findUserByEmail(userName);
        HttpSession session = request.getSession();
        if (theUser != null) {
            if(theUser.getImages() != null) {
                session.setAttribute("image",theUser.getImages().getImageUrl());
            }
            session.setAttribute("USERNAME", theUser.getFullName());
            session.setAttribute("id", theUser.getUserId());
        } else {
            System.err.println("User not found in database: " + userName);
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_REALTOR")) {
                response.sendRedirect(request.getContextPath() + "/home/home");
                return;
            } else if (authority.getAuthority().equals("ROLE_ADMIN")) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                return;
            }
        }
        System.err.println("Error here");
        response.sendRedirect(request.getContextPath() + "/login?error=true");
    }


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
                session.removeAttribute("USERNAME");
                System.out.println("User has been logged out");
            }
        }
        response.sendRedirect(request.getContextPath() + "/login?logout=true");
    }

}

