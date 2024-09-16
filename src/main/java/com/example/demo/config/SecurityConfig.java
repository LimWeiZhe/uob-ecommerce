package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// @Configuration marks the class a configuration for some aspect of Springboot
@Configuration
// @enablewebsecurity tells java this class is for web security
@EnableWebSecurity
public class SecurityConfig {
    // a @bean method returns a component that is used by Spring Boot 
    // think of it as a factory method that spring boot will use later, sometimes internally
    // to create an instance of a component that it needs
    // if springboot ever needs an object of the class 'SecurityFilterChain', it will call this method
    @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // the 'httpSecurity' class allows us to define secutity rules
       http
           .authorizeHttpRequests(authz -> authz
                // if the url starts with /register,/login, /css or /js, can be accessed without login
                // if not the cannot even login 
               .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll()
               .anyRequest().authenticated() // switch to .permitAll() to allow access , .authenticated()
           )
           // specify where the login form will be
           .formLogin(form -> form
               .loginPage("/login")
               .permitAll()
           )
           .logout(logout -> logout
               .permitAll()
           );
       return http.build();
   }

    @Bean
   public PasswordEncoder passwordEncoder() {
        // Bcrypt is one of the password encryption algorithm
       return new BCryptPasswordEncoder();
   }
}