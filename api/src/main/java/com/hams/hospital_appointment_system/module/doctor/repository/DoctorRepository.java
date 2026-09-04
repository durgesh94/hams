package com.hams.hospital_appointment_system.module.doctor.repository;
import com.hams.hospital_appointment_system.module.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    boolean existsByEmail(String email);
    
}
