package com.hams.doctor.controller;

import com.hams.doctor.service.ServiceTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/internal/test")
@RequiredArgsConstructor
public class InternalTestController {

    private final ServiceTokenProvider serviceTokenProvider;

    @GetMapping("/token")
    public ResponseEntity<String> getToken() {

        String token = serviceTokenProvider.getToken();

        return ResponseEntity.ok(token);
    }
}