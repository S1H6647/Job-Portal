package com.project.jobportal.web.controller;

import com.project.jobportal.service.UserService;
import com.project.jobportal.web.dto.auth.LoginRequest;
import com.project.jobportal.web.dto.auth.LoginResponse;
import com.project.jobportal.web.dto.auth.RegisterRequest;
import com.project.jobportal.web.dto.auth.RegisterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request
    ) {
        var response = userService.registerUser(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        var response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }
}
