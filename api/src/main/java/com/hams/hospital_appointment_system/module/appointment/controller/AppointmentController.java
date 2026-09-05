package com.hams.hospital_appointment_system.module.appointment.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

import com.hams.hospital_appointment_system.common.response.ApiResponse;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentRequest;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentResponse;
import com.hams.hospital_appointment_system.module.appointment.service.AppointmentService;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(@RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentService.createAppointment(request);
        ApiResponse<AppointmentResponse> apiResponse = ApiResponse.<AppointmentResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Appointment created successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(@PathVariable("id") Long appointmentId) {
        AppointmentResponse response = appointmentService.getAppointmentById(appointmentId);
        ApiResponse<AppointmentResponse> apiResponse = ApiResponse.<AppointmentResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Appointment retrieved successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
