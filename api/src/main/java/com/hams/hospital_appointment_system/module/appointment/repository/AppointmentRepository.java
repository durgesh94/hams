package com.hams.hospital_appointment_system.module.appointment.repository;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.hams.hospital_appointment_system.module.appointment.entity.Appointment;
import com.hams.hospital_appointment_system.module.appointment.entity.AppointmentStatus;

public interface AppointmentRepository extends
                JpaRepository<Appointment, Long>,
                JpaSpecificationExecutor<Appointment> {

        List<Appointment> findByDoctorIdAndAppointmentDate(
                        Long doctorId,
                        LocalDate appointmentDate);

        List<Appointment> findByPatientId(Long patientId);

        List<Appointment> findByDoctorId(Long doctorId);

        List<Appointment> findByStatus(AppointmentStatus status);

        // deprecated: use existsPatientOverlappingAppointment or
        // existsDoctorOverlappingAppointment instead
        boolean existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusIn(
                        Long doctorId,
                        LocalDate appointmentDate,
                        LocalTime appointmentTime,
                        List<AppointmentStatus> statuses);

        long countByDoctorId(Long doctorId);

        // Check if a patient has overlapping appointments within the specified time
        // range and statuses
        // Returns true if there is at least one overlapping appointment
        @Query("""
                            SELECT COUNT(a) > 0
                            FROM Appointment a
                            WHERE a.patient.id = :patientId
                              AND a.appointmentDate = :appointmentDate
                              AND a.appointmentTime < :newEndTime
                              AND a.appointmentEndTime > :newStartTime
                              AND a.status IN :statuses
                        """)
        boolean existsPatientOverlappingAppointment(
                        @Param("patientId") Long patientId,
                        @Param("appointmentDate") LocalDate appointmentDate,
                        @Param("newStartTime") LocalTime newStartTime,
                        @Param("newEndTime") LocalTime newEndTime,
                        @Param("statuses") List<AppointmentStatus> statuses);

        // Check if a doctor has overlapping appointments within the specified time
        // range and statuses
        // Returns true if there is at least one overlapping appointment
        @Query("""
                            SELECT COUNT(a) > 0
                            FROM Appointment a
                            WHERE a.doctor.id = :doctorId
                              AND a.appointmentDate = :appointmentDate
                              AND a.appointmentTime < :newEndTime
                              AND a.appointmentEndTime > :newStartTime
                              AND a.status IN :statuses
                        """)
        boolean existsDoctorOverlappingAppointment(
                        @Param("doctorId") Long doctorId,
                        @Param("appointmentDate") LocalDate appointmentDate,
                        @Param("newStartTime") LocalTime newStartTime,
                        @Param("newEndTime") LocalTime newEndTime,
                        @Param("statuses") List<AppointmentStatus> statuses);
}
