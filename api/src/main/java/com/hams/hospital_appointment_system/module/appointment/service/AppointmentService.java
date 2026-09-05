package com.hams.hospital_appointment_system.module.appointment.service;

import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentRequest;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentResponse;

public interface AppointmentService {

    AppointmentResponse createAppointment(AppointmentRequest request);
    
}