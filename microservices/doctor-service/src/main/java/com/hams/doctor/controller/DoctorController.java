package com.hams.doctor.controller;

import com.hams.doctor.dto.CreateDoctorRequest;
import com.hams.doctor.dto.DoctorResponse;
import com.hams.doctor.dto.UpdateDoctorRequest;
import com.hams.doctor.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(
            @Valid @RequestBody CreateDoctorRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(doctorService.createDoctor(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctor(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                doctorService.getDoctor(id)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DoctorResponse> getDoctorByUserId(
            @PathVariable UUID userId
    ) {

        return ResponseEntity.ok(
                doctorService.getDoctorByUserId(userId)
        );
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {

        return ResponseEntity.ok(
                doctorService.getAllDoctors()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDoctorRequest request
    ) {

        return ResponseEntity.ok(
                doctorService.updateDoctor(id, request)
        );
    }
}