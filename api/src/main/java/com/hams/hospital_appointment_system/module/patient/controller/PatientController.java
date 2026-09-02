package com.hams.hospital_appointment_system.module.patient.controller;

import com.hams.hospital_appointment_system.common.response.ApiResponse;
import com.hams.hospital_appointment_system.module.patient.dto.PatientRequest;
import com.hams.hospital_appointment_system.module.patient.dto.PatientResponse;
import com.hams.hospital_appointment_system.module.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<ApiResponse<PatientResponse>> createPatient(@Valid @RequestBody PatientRequest patientRequest) {
        PatientResponse patientResponse = patientService.createPatient(patientRequest);
        ApiResponse<PatientResponse> response = ApiResponse.<PatientResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Patient created successfully")
                .data(patientResponse)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientResponse>>> getAllPatients(){
        List<PatientResponse> patientResponseList = patientService.getAllPatients();
        ApiResponse<List<PatientResponse>> response = ApiResponse.<List<PatientResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Fetched all patients")
                .data(patientResponseList)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
