package com.hams.hospital_appointment_system.module.patient.repository;

import com.hams.hospital_appointment_system.module.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
