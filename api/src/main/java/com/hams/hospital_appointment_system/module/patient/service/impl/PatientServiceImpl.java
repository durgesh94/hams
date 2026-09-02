package com.hams.hospital_appointment_system.module.patient.service.impl;

import com.hams.hospital_appointment_system.common.exception.DuplicateResourceException;
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
        return null;
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
    public List<PatientResponse> updatePatientById(Long id) {
        return List.of();
    }

    @Override
    public PatientResponse deletePatientById(Long id) {
        return null;
    }
}
