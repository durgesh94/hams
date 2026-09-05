package com.hams.hospital_appointment_system.module.appointment.dto;

import java.time.LocalDate;

import com.hams.hospital_appointment_system.module.appointment.entity.AppointmentStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AppointmentFilter {

    private Long patientId;
    private Long doctorId;
    private AppointmentStatus status;
    private LocalDate date;

}
