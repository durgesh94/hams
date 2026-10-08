package com.hams.doctor.controller;

import com.hams.doctor.client.AuthClient;
import com.hams.doctor.client.dto.AuthUserResponse;
import com.hams.doctor.client.dto.CreateUserRequest;
import com.hams.doctor.service.ServiceTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/internal/test")
@RequiredArgsConstructor
public class InternalTestController {

    private final ServiceTokenProvider serviceTokenProvider;
    private final AuthClient authClient;

    @GetMapping("/token")
    public ResponseEntity<String> getToken() {

        String token = serviceTokenProvider.getToken();

        return ResponseEntity.ok(token);
    }

    @PostMapping("/create-user")
    public ResponseEntity<AuthUserResponse> createUser() {

        CreateUserRequest request = new CreateUserRequest(
                "doctor",
                "Doctor@123",
                "DOCTOR"
        );

        AuthUserResponse response =
                authClient.createUser(request);

        return ResponseEntity.ok(response);
    }
}