package com.hams.doctor.controller;

import com.hams.doctor.dto.CreateDoctorRequest;
import com.hams.doctor.dto.CreateProfileRequest;
import com.hams.doctor.dto.DoctorResponse;
import com.hams.doctor.dto.DoctorReviewRequest;
import com.hams.doctor.dto.UpdateDoctorRequest;
import com.hams.doctor.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
                        @Valid @RequestBody CreateDoctorRequest request) {
                DoctorResponse response = doctorService.createDoctor(request);
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @PostMapping("/me/profile")
        public ResponseEntity<DoctorResponse> createProfile(
                        @AuthenticationPrincipal Jwt jwt,
                        @Valid @RequestBody CreateProfileRequest request) {

                UUID userId = UUID.fromString(jwt.getSubject());

                DoctorResponse response = doctorService.createProfile(request, userId);
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<DoctorResponse> getDoctor(
                        @PathVariable UUID id) {
                DoctorResponse response = doctorService.getDoctor(id);
                return ResponseEntity.ok(response);
        }

        @GetMapping("/user/{userId}")
        public ResponseEntity<DoctorResponse> getDoctorByUserId(
                        @PathVariable UUID userId) {
                DoctorResponse response = doctorService.getDoctorByUserId(userId);
                return ResponseEntity.ok(response);
        }

        @GetMapping
        public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
                List<DoctorResponse> response = doctorService.getAllDoctors();
                return ResponseEntity.ok(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<DoctorResponse> updateDoctor(
                        @PathVariable UUID id,
                        @Valid @RequestBody UpdateDoctorRequest request) {

                DoctorResponse response = doctorService.updateDoctor(id, request);
                return ResponseEntity.ok(response);
        }

        @PatchMapping("/{doctorId}/review")
        public ResponseEntity<DoctorResponse> reviewDoctor(
                        @PathVariable UUID doctorId,
                        @Valid @RequestBody DoctorReviewRequest request) {

                DoctorResponse response = doctorService.reviewDoctor(doctorId, request);

                return ResponseEntity.ok(response);
        }
}