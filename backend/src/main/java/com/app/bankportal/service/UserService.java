package com.app.bankportal.service;

import com.app.bankportal.dto.*;
import com.app.bankportal.model.User;
import com.app.bankportal.repository.UserRepository;

import javax.management.RuntimeErrorException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.app.bankportal.dto.LoginRequest;
import com.app.bankportal.security.JwtService;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        
    }

    public User signup(SignupRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return user;

    }
    public String login(LoginRequest request) {

        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if(!user.isPresent()){
            throw new RuntimeException("User Does not exist");
        }
        User userFound = user.get();

        if (passwordEncoder.matches(request.getPassword(), userFound.getPassword())) {
            return jwtService.generateToken(userFound.getUsername());
        }else {
            throw new RuntimeException("Invalid username or password");
        }
    }
}