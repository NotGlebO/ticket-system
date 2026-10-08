package com.glebs.ticket_system;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.GET, "/my-tickets")
            .authenticated()

            // Главная, Login и CSS доступны всем
            .requestMatchers(
                "/",
                "/login",
                "/access-denied",
                "/css/**",
                "/js/**",
                "/images/**"
            )
            .permitAll()
            // Создать тикет может любой человек, даже без аккаунта
            .requestMatchers(HttpMethod.GET, "/tickets/new")
            .permitAll()

            .requestMatchers(HttpMethod.POST, "/tickets")
            .permitAll()

            // Админ-панель только ADMIN
            .requestMatchers("/admin/**")
            .hasRole("ADMIN")

            .requestMatchers(HttpMethod.POST, "/tickets/*/take")
            .hasAnyRole("IT", "ADMIN")

            .requestMatchers(HttpMethod.POST, "/tickets/*/release")
            .hasAnyRole("IT", "ADMIN")

            .requestMatchers(HttpMethod.POST, "/tickets/*/complete")
            .hasAnyRole("IT", "ADMIN")
            
            // Смотреть список тикетов могут IT и ADMIN
            .requestMatchers(HttpMethod.GET, "/tickets")
            .hasAnyRole("IT", "ADMIN")

            .requestMatchers(HttpMethod.GET, "/tickets/*")
            .authenticated()

            .requestMatchers(HttpMethod.POST, "/tickets/*/messages")
            .authenticated()
            // Всё остальное требует входа
            .anyRequest()
            .authenticated()
        )
        

            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
            )

            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            )

            .exceptionHandling(exception -> exception
            .accessDeniedPage("/access-denied")
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}