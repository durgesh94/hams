package com.hams.hospital_appointment_system.module.patient.controller;

import com.hams.hospital_appointment_system.module.patient.dto.PatientRequest;
import com.hams.hospital_appointment_system.module.patient.dto.PatientResponse;
import com.hams.hospital_appointment_system.module.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody PatientRequest patientRequest) {
        PatientResponse patientResponse = patientService.createPatient(patientRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(patientResponse);
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatient(){
        List<PatientResponse> patientResponseList = patientService.getAllPatient();
        return ResponseEntity.ok(patientResponseList);
    }
}
