package com.hams.hospital_appointment_system.module.appointment.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import lombok.RequiredArgsConstructor;

import com.hams.hospital_appointment_system.common.response.ApiResponse;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentFilter;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentRequest;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentResponse;
import com.hams.hospital_appointment_system.module.appointment.entity.AppointmentStatus;
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

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateAppointment(@PathVariable("id") Long appointmentId,
            @RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentService.updateAppointment(appointmentId, request);
        ApiResponse<AppointmentResponse> apiResponse = ApiResponse.<AppointmentResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Appointment updated successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateAppointmentStatus(
            @PathVariable("id") Long appointmentId,
            @RequestParam("status") AppointmentStatus status) {
        AppointmentResponse response = appointmentService.updateAppointmentStatus(appointmentId, status);
        ApiResponse<AppointmentResponse> apiResponse = ApiResponse.<AppointmentResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Appointment status updated successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<AppointmentResponse>>> getAppointments(
            @ModelAttribute AppointmentFilter filter,
            Pageable pageable) {
        Page<AppointmentResponse> response = appointmentService.getAppointments(filter, pageable);
        ApiResponse<Page<AppointmentResponse>> apiResponse = ApiResponse.<Page<AppointmentResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Appointments retrieved successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
