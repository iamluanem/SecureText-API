package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Desativa CSRF para facilitar o desenvolvimento
            .headers(headers -> headers
                .frameOptions(frame -> frame.disable())) // Permite o uso do H2 Console
            .authorizeHttpRequests(auth -> 
                auth.requestMatchers("/h2-console/**").permitAll() // Permite acesso ao H2 Console
                    .anyRequest().permitAll() // Permite todas as outras requisições
            );
        return http.build();
    }
}
