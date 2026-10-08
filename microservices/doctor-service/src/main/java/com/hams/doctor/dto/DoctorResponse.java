package com.hams.doctor.dto;

import com.hams.doctor.entity.DoctorStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record DoctorResponse(
        UUID id,
        UUID userId,
        String firstName,
        String lastName,
        String specialization,
        String qualification,
        Integer experience,
        String phone,
        String email,
        String address,
        DoctorStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}