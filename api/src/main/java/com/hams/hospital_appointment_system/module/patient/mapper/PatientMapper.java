package com.hams.hospital_appointment_system.module.patient.mapper;

import com.hams.hospital_appointment_system.module.patient.entity.Patient;
import com.hams.hospital_appointment_system.module.patient.dto.PatientRequest;
import com.hams.hospital_appointment_system.module.patient.dto.PatientResponse;

public class PatientMapper {
    
    private PatientMapper() {
        // Private constructor to prevent instantiation
    }

    public static Patient toEntity(PatientRequest patientRequest) {
        return Patient.builder()
                .firstName(patientRequest.getFirstName())
                .lastName(patientRequest.getLastName())
                .gender(patientRequest.getGender())
                .dateOfBirth(patientRequest.getDateOfBirth())
                .email(patientRequest.getEmail())
                .phone(patientRequest.getPhone())
                .build();
    }

    public static PatientResponse toDto(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .gender(patient.getGender())
                .dateOfBirth(patient.getDateOfBirth())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .build();
    }
}
