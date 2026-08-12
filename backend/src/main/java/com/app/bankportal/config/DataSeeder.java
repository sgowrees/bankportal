package com.app.bankportal.config;

import com.app.bankportal.dto.SignupRequest;
import com.app.bankportal.repository.UserRepository;
import com.app.bankportal.service.UserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    ApplicationRunner seedDemoUser(UserService userService, UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("demo").isEmpty()) {
                SignupRequest request = new SignupRequest();
                request.setUsername("demo");
                request.setPassword("demo123");
                userService.signup(request);
            }
        };
    }
}
