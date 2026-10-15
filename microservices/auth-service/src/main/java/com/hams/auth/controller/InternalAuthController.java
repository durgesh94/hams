package com.hams.auth.controller;

import com.hams.auth.dto.internal.ServiceTokenRequest;
import com.hams.auth.dto.internal.ServiceTokenResponse;
import com.hams.auth.service.ServiceAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/auth")
@RequiredArgsConstructor
public class InternalAuthController {

    private final ServiceAuthService serviceAuthService;

    @PostMapping("/token")
    public ResponseEntity<ServiceTokenResponse> createToken(
            @Valid @RequestBody ServiceTokenRequest request
    ) {
        return ResponseEntity.ok(
                serviceAuthService.createServiceToken(request)
        );
    }
}