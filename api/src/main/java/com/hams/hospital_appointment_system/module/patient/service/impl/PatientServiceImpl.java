package com.hams.hospital_appointment_system.module.patient.service.impl;

import com.hams.hospital_appointment_system.common.exception.DuplicateResourceException;
import com.hams.hospital_appointment_system.common.exception.ResourceNotFoundException;
import com.hams.hospital_appointment_system.module.patient.dto.PatientRequest;
import com.hams.hospital_appointment_system.module.patient.dto.PatientResponse;
import com.hams.hospital_appointment_system.module.patient.entity.Patient;
import com.hams.hospital_appointment_system.module.patient.mapper.PatientMapper;
import com.hams.hospital_appointment_system.module.patient.repository.PatientRepository;
import com.hams.hospital_appointment_system.module.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public PatientResponse createPatient(PatientRequest patientRequest) {
        if(patientRepository.existsByEmail(patientRequest.getEmail())){
            throw new DuplicateResourceException("Patient with email "+patientRequest.getEmail()+" already exists");
        }
        Patient patient = PatientMapper.toEntity(patientRequest);
        Patient savedPatient = patientRepository.save(patient);
        return PatientMapper.toDto(savedPatient);
    }

    @Override
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id '" + id + "' not found"));
        return PatientMapper.toDto(patient);
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        List<Patient> patientList = patientRepository.findAll();
        return List.of(
            patientList.stream()
                    .map(PatientMapper::toDto)
                    .toArray(PatientResponse[]::new)
        );
    }

    @Override
    public PatientResponse updatePatientById(Long id, PatientRequest patientRequest) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id '" + id + "' not found"));
        Patient updatedPatient = PatientMapper.updateEntity(patient, patientRequest);
        Patient savedPatient = patientRepository.save(updatedPatient);
        return PatientMapper.toDto(savedPatient);
    }

    @Override
    public PatientResponse deletePatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id '" + id + "' not found"));
        patientRepository.delete(patient);
        return PatientMapper.toDto(patient);
    }
}
