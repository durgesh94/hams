package com.hams.hospital_appointment_system.module.doctor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.hams.hospital_appointment_system.common.response.ApiResponse;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorFilterRequest;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorRequest;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorResponse;

import java.time.LocalDateTime;
import java.util.List;
import com.hams.hospital_appointment_system.module.doctor.service.DoctorService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAllDoctors() {
        List<DoctorResponse> doctors = doctorService.getAllDoctors();
        ApiResponse<List<DoctorResponse>> response = ApiResponse.<List<DoctorResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Fetched all doctors successfully.")
                .data(doctors)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(@RequestBody DoctorRequest doctorRequest) {
        DoctorResponse doctorResponse = doctorService.createDoctor(doctorRequest);

        ApiResponse<DoctorResponse> response = ApiResponse.<DoctorResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor created successfully.")
                .data(doctorResponse)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(@PathVariable Long id) {
        DoctorResponse doctorResponse = doctorService.getDoctorById(id);

        ApiResponse<DoctorResponse> response = ApiResponse.<DoctorResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Fetched doctor successfully.")
                .data(doctorResponse)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(@PathVariable Long id,
            @RequestBody DoctorRequest doctorRequest) {
        DoctorResponse doctorResponse = doctorService.updateDoctor(id, doctorRequest);

        ApiResponse<DoctorResponse> response = ApiResponse.<DoctorResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor updated successfully.")
                .data(doctorResponse)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Doctor deleted successfully.")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getByFilter(
            @ModelAttribute DoctorFilterRequest filterRequest) {
        List<DoctorResponse> doctors = doctorService.getDoctorsByFilter(filterRequest);
        ApiResponse<List<DoctorResponse>> response = ApiResponse.<List<DoctorResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Fetched doctors by filter successfully.")
                .data(doctors)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
