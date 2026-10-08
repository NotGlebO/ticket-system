package com.glebs.ticket_system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${APP_ADMIN_PASSWORD:}") String adminPassword) {

        return args -> {

            // Создаём администратора только при наличии пароля
            if (!adminPassword.isBlank()
                    && userRepository.findByUsername("admin").isEmpty()) {

                userRepository.save(new User(
                        "admin",
                        passwordEncoder.encode(adminPassword),
                        Role.ADMIN
                ));

                System.out.println("Admin account created");
            }

            // Демонстрационный IT-пользователь
            if (userRepository.findByUsername("it").isEmpty()) {

                userRepository.save(new User(
                        "it",
                        passwordEncoder.encode("it"),
                        Role.IT
                ));

                System.out.println("IT demo account created");
            }

            // Демонстрационный обычный пользователь
            if (userRepository.findByUsername("user").isEmpty()) {

                userRepository.save(new User(
                        "user",
                        passwordEncoder.encode("user"),
                        Role.USER
                ));

                System.out.println("User demo account created");
            }
        };
    }
}