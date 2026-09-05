package com.hams.hospital_appointment_system.module.appointment.service.impl;

import com.hams.hospital_appointment_system.common.exception.AppointmentSlotAlreadyBookedException;
import com.hams.hospital_appointment_system.common.exception.ResourceNotFoundException;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentFilter;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentRequest;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentResponse;
import com.hams.hospital_appointment_system.module.appointment.entity.Appointment;
import com.hams.hospital_appointment_system.module.appointment.entity.AppointmentStatus;
import com.hams.hospital_appointment_system.module.appointment.mapper.AppointmentMapper;
import com.hams.hospital_appointment_system.module.appointment.repository.AppointmentRepository;
import com.hams.hospital_appointment_system.module.appointment.service.AppointmentService;
import com.hams.hospital_appointment_system.module.appointment.specification.AppointmentSpecification;
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
                .orElseThrow(
                        () -> new ResourceNotFoundException("Patient not found with id " + request.getPatientId()));

        // Step 2: Check doctor status
        if (doctor.getStatus() != DoctorStatus.ACTIVE) {
            throw new AppointmentSlotAlreadyBookedException(
                    "Doctor with id " + request.getDoctorId() + " is not available");
        }

        // Step 3: Check if the doctor is available at the requested appointment date
        boolean alreadyBooked = appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusIn(
                request.getDoctorId(),
                request.getAppointmentDate(),
                request.getAppointmentTime(),
                List.of(AppointmentStatus.BOOKED, AppointmentStatus.CONFIRMED));
        if (alreadyBooked) {
            throw new AppointmentSlotAlreadyBookedException(
                    "Doctor with id " + request.getDoctorId() + " is not available at the requested appointment date");
        }

        // Step 4: Create the appointment entity
        Appointment appointment = AppointmentMapper.toEntity(request, doctor, patient);

        // Step 5: Save the appointment entity to the database
        appointment = appointmentRepository.save(appointment);

        // Step 6: Convert the saved appointment entity to a response DTO
        return AppointmentMapper.toDto(appointment);
    }

    @Override
    public AppointmentResponse getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + appointmentId));
        return AppointmentMapper.toDto(appointment);
    }

    @Override
    public AppointmentResponse updateAppointment(Long appointmentId, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + appointmentId));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + request.getDoctorId()));
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Patient not found with id " + request.getPatientId()));

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setNotes(request.getNotes());
        appointment.setUpdatedAt(LocalDateTime.now());

        appointment = appointmentRepository.save(appointment);
        return AppointmentMapper.toDto(appointment);
    }

    @Override
    public AppointmentResponse updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + appointmentId));

        appointment.setStatus(status);
        appointment.setUpdatedAt(LocalDateTime.now());

        appointment = appointmentRepository.save(appointment);
        return AppointmentMapper.toDto(appointment);
    }

    @Override
    public List<AppointmentResponse> getAppointments(AppointmentFilter filter) {
        return appointmentRepository.findAll(AppointmentSpecification.filter(filter))
                .stream()
                .map(AppointmentMapper::toDto)
                .toList();
    }
}
