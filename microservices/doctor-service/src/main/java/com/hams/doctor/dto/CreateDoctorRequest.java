package com.hams.doctor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateDoctorRequest(

        UUID userId,

        @NotBlank(message = "First name is required")
        @Size(max = 100, message = "First name must not exceed 100 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 100, message = "Last name must not exceed 100 characters")
        String lastName,

        @NotBlank(message = "Specialization is required")
        @Size(max = 150, message = "Specialization must not exceed 150 characters")
        String specialization,

        @NotBlank(message = "Qualification is required")
        @Size(max = 200, message = "Qualification must not exceed 200 characters")
        String qualification,

        @Min(value = 0, message = "Experience cannot be negative")
        @Max(value = 60, message = "Experience cannot exceed 60 years")
        Integer experience,

        @Size(max = 20, message = "Phone must not exceed 20 characters")
        String phone,

        @Email(message = "Invalid email format")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        String email,

        String address
) {
}