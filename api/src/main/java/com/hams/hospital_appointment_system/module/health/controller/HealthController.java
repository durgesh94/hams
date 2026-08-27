package com.hams.hospital_appointment_system.module.health.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, String>> health(){
        return ResponseEntity.ok(
                Map.of(
                        "status", "Up",
                        "message", "Hospital Appointment System is running."
                )
        );
    }
}
