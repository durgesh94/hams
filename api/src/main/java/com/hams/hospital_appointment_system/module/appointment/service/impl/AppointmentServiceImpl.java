package com.hams.hospital_appointment_system.module.appointment.service.impl;
import com.hams.hospital_appointment_system.common.exception.AppointmentSlotAlreadyBookedException;
import com.hams.hospital_appointment_system.common.exception.ResourceNotFoundException;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentRequest;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentResponse;
import com.hams.hospital_appointment_system.module.appointment.entity.Appointment;
import com.hams.hospital_appointment_system.module.appointment.entity.AppointmentStatus;
import com.hams.hospital_appointment_system.module.appointment.repository.AppointmentRepository;
import com.hams.hospital_appointment_system.module.appointment.service.AppointmentService;
import com.hams.hospital_appointment_system.module.doctor.entity.Doctor;
import com.hams.hospital_appointment_system.module.doctor.entity.DoctorStatus;
import com.hams.hospital_appointment_system.module.doctor.repository.DoctorRepository;
import com.hams.hospital_appointment_system.module.patient.entity.Patient;
import com.hams.hospital_appointment_system.module.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        // Step 1: Retrieve the doctor and patient entities based on the request
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + request.getDoctorId()));
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + request.getPatientId()));

        // Step 1.5: Check doctor status
        if (doctor.getStatus() != DoctorStatus.ACTIVE) {
            throw new AppointmentSlotAlreadyBookedException("Doctor with id " + request.getDoctorId() + " is not available");
        }

        // Step 2: Check if the doctor is available at the requested appointment date
        boolean alreadyBooked = appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusIn(
                request.getDoctorId(),
                request.getAppointmentDate(),
                request.getAppointmentTime(),
                List.of(AppointmentStatus.BOOKED, AppointmentStatus.CONFIRMED)
        );
        if (alreadyBooked) {
            throw new AppointmentSlotAlreadyBookedException("Doctor with id " + request.getDoctorId() + " is not available at the requested appointment date");
        }

        // Step 2: Create and save the appointment entity
        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .reason(request.getReason())
                .notes(request.getNotes())
                .status(AppointmentStatus.BOOKED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        appointment = appointmentRepository.save(appointment);

        // Step 3: Convert the saved appointment entity to a response DTO
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .doctorId(doctor.getId())
                .doctorName(doctor.getFirstName() + " " + doctor.getLastName())
                .patientId(patient.getId())
                .patientName(patient.getFirstName() + " " + patient.getLastName())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .reason(appointment.getReason())
                .notes(appointment.getNotes())
                .status(appointment.getStatus())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }

}
