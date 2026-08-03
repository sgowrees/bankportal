package com.app.bankportal.controller;

import com.app.bankportal.dto.LoginRequest;
import com.app.bankportal.dto.LoginResponse;
import com.app.bankportal.dto.SignupRequest;
import com.app.bankportal.model.User;
import com.app.bankportal.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        User user = userService.signup(request);
        return ResponseEntity.ok("Success in signing up");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        String token = userService.login(request);

        User user = userService.findByUsername(request.getUsername());

        LoginResponse response = new LoginResponse(
                token,
                user.getId()
        );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication){
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }
}