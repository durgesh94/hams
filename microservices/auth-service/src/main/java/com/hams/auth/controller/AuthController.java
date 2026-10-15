package com.hams.auth.controller;

import com.hams.auth.dto.LoginRequest;
import com.hams.auth.dto.LoginResponse;
import com.hams.auth.dto.RegisterRequest;
import com.hams.auth.dto.UserResponse;
import com.hams.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(
            Authentication authentication
    ){
        UserResponse userResponse = authService.getCurrentUser(authentication.getName());
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/register/patient")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request
            ){
        UserResponse userResponse = authService.createUser(request, "PATIENT");
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<UserResponse> registerDoctor(
            @Valid @RequestBody RegisterRequest request
    ){
        UserResponse userResponse = authService.createUser(request, "DOCTOR");
        return ResponseEntity.ok(userResponse);
    }
}