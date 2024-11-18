package com.example.Final.configuration;

import com.example.Final.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@EnableWebSecurity
public class UserConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, UserSuccessHandler userSuccessHandler) throws Exception {
        httpSecurity.authorizeHttpRequests(config -> config
                        .requestMatchers("/").hasRole("REALTOR")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/showRegister", "/checkRegisterCustomer", "/showLogin", "/forgotPassword", "/resetPassword").permitAll()
//                        .anyRequest().authenticated())
                        .anyRequest().permitAll())

                .formLogin(login -> login
                        .loginPage("/showLogin")
                        .loginProcessingUrl("/login")
                        .successHandler(userSuccessHandler)
                        .failureUrl("/showLogin?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(userSuccessHandler)
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"));
        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("*/image/**", "*/css/**", "*/js/**");
    }

}
