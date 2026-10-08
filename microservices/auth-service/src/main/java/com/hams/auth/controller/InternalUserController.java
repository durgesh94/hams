package com.hams.auth.controller;

import com.hams.auth.dto.RegisterRequest;
import com.hams.auth.dto.UserResponse;
import com.hams.auth.dto.internal.CreateInternalUserRequest;
import com.hams.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateInternalUserRequest request) {

        RegisterRequest registerRequest = new RegisterRequest(request.username(),request.password());
        UserResponse userResponse = authService.createUser(
                registerRequest,
                request.role()
            );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userResponse);
    }
}