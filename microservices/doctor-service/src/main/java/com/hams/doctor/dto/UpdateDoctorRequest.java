package com.hams.doctor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateDoctorRequest(

        @Size(max = 100, message = "First name must not exceed 100 characters")
        String firstName,

        @Size(max = 100, message = "Last name must not exceed 100 characters")
        String lastName,

        @Size(max = 150, message = "Specialization must not exceed 150 characters")
        String specialization,

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