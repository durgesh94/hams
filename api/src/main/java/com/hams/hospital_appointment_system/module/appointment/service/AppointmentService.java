package com.hams.hospital_appointment_system.module.appointment.service;

import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentFilter;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentRequest;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hams.hospital_appointment_system.module.appointment.entity.AppointmentStatus;

public interface AppointmentService {

    AppointmentResponse createAppointment(AppointmentRequest request);

    AppointmentResponse getAppointmentById(Long appointmentId);

    AppointmentResponse updateAppointment(Long appointmentId, AppointmentRequest request);

    AppointmentResponse updateAppointmentStatus(Long appointmentId, AppointmentStatus status);

    Page<AppointmentResponse> getAppointments(AppointmentFilter filter, Pageable pageable);

}