package com.hams.hospital_appointment_system.module.patient.service;

import com.hams.hospital_appointment_system.module.patient.dto.PatientRequest;
import com.hams.hospital_appointment_system.module.patient.dto.PatientResponse;

import java.util.List;

public interface PatientService {

    PatientResponse createPatient(PatientRequest patientRequest);

    PatientResponse getPatientById(Long id);

    List<PatientResponse> getAllPatient();

    List<PatientResponse> updatePatientById(Long id);

    PatientResponse deletePatientById(Long id);

}