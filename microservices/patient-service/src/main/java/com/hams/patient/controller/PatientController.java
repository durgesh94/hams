package com.hams.patient.controller;

import com.hams.patient.dto.PatientRequest;
import com.hams.patient.dto.PatientResponse;
import com.hams.patient.service.PatientService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(
            @Valid @RequestBody PatientRequest request) {

        PatientResponse response = patientService.createPatient(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // READ - Get all patients
    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {

        List<PatientResponse> patients = patientService.getAllPatients();

        return ResponseEntity.ok(patients);
    }

    // READ - Get patient by ID
    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponse> getPatientById(
            @PathVariable UUID patientId) {

        PatientResponse response = patientService.getPatientById(patientId);

        return ResponseEntity.ok(response);
    }

    // UPDATE
    @PutMapping("/{patientId}")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable UUID patientId,
            @Valid @RequestBody PatientRequest request) {

        PatientResponse response = patientService.updatePatient(patientId, request);

        return ResponseEntity.ok(response);
    }

    // DELETE
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatient(
            @PathVariable UUID patientId) {

        patientService.deletePatient(patientId);

        return ResponseEntity.noContent().build();
    }
}