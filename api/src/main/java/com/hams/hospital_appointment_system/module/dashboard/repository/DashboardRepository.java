package com.hams.hospital_appointment_system.module.dashboard.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hams.hospital_appointment_system.module.doctor.entity.Doctor;
import com.hams.hospital_appointment_system.module.patient.entity.Patient;
import com.hams.hospital_appointment_system.module.appointment.entity.Appointment;

public interface DashboardRepository extends JpaRepository<Doctor, Long> {

    @Query("""
            SELECT COUNT(d)
            FROM Doctor d
            WHERE d.status = 'ACTIVE'
            """)
    long countActiveDoctors();

    @Query("""
            SELECT COUNT(p)
            FROM Patient p
            WHERE p.createdAt >= :startDate
              AND p.createdAt < :endDate
            """)
    long countNewPatients(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("""
            SELECT COUNT(a)
            FROM Appointment a
            WHERE a.createdAt >= :startDate
              AND a.createdAt < :endDate
            """)
    long countAppointments(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
