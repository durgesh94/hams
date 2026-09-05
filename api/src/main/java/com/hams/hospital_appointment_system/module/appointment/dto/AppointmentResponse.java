package com.hams.hospital_appointment_system.module.appointment.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import com.hams.hospital_appointment_system.module.appointment.entity.AppointmentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {
    private Long id;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    private String reason;
    private String notes;

    private AppointmentStatus status;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
