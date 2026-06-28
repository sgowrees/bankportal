package com.app.bankportal.controller;

import com.app.bankportal.dto.*;
import com.app.bankportal.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.app.bankportal.model.User;


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
        return ResponseEntity.ok("Sucess in signing up");
        
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request){
        String token = userService.login(request);
        return ResponseEntity.ok(token);
    }
}