package com.hams.patient.service;

import com.hams.patient.dto.PatientRequest;
import com.hams.patient.dto.PatientResponse;
import com.hams.patient.entity.Patient;
import com.hams.patient.exception.PatientNotFoundException;
import com.hams.patient.repository.PatientRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // CREATE
    public PatientResponse createPatient(PatientRequest request) {
        Patient patient = new Patient();

        applyRequestToPatient(patient, request);

        Patient savedPatient = patientRepository.save(patient);

        return mapToResponse(savedPatient);
    }

    // READ - Get all patients
    @Transactional(readOnly = true)
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // READ - Get patient by ID
    @Transactional(readOnly = true)
    public PatientResponse getPatientById(UUID patientId) {
        Patient patient = findPatientById(patientId);

        return mapToResponse(patient);
    }

    // UPDATE
    public PatientResponse updatePatient(
            UUID patientId,
            PatientRequest request) {

        Patient patient = findPatientById(patientId);

        applyRequestToPatient(patient, request);

        Patient updatedPatient = patientRepository.save(patient);

        return mapToResponse(updatedPatient);
    }

    // DELETE
    public void deletePatient(UUID patientId) {
        Patient patient = findPatientById(patientId);

        patientRepository.delete(patient);
    }

    // Find patient or throw exception
    private Patient findPatientById(UUID patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
    }

    // Copy request fields into the entity
    private void applyRequestToPatient(
            Patient patient,
            PatientRequest request) {

        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setAddress(request.getAddress());
    }

    // Convert entity into response DTO
    private PatientResponse mapToResponse(Patient patient) {
        PatientResponse response = new PatientResponse();

        response.setId(patient.getId());
        response.setUserId(patient.getUserId());
        response.setFirstName(patient.getFirstName());
        response.setLastName(patient.getLastName());
        response.setDateOfBirth(patient.getDateOfBirth());
        response.setGender(patient.getGender());
        response.setBloodGroup(patient.getBloodGroup());
        response.setPhoneNumber(patient.getPhoneNumber());
        response.setAddress(patient.getAddress());
        response.setCreatedAt(patient.getCreatedAt());
        response.setUpdatedAt(patient.getUpdatedAt());

        return response;
    }
}