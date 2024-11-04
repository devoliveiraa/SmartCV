package com.smartcv.smartcv.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((request) -> request
                        .requestMatchers(
                                "/",
                                "/SmartCV",
                                "/SmartCV/login",
                                "/SmartCV/profilej",
                                "/SmartCV/register",
                                "/SmartCV/register",
                                "/SmartCV/profile",
                                "/SmartCV/profileEdit",
                                "/start/**",
                                "/profile/**",
                                "/login/**",
                                "/register/**"
                        ).permitAll());
                       /* .anyRequest().authenticated());  // Proteger outras rotas*/
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}