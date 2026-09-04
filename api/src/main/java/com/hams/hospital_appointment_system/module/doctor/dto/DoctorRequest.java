package com.hams.hospital_appointment_system.module.doctor.dto;

import com.hams.hospital_appointment_system.common.enums.Gender;

import jakarta.validation.constraints.NotBlank;
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
public class DoctorRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    @NotBlank(message = "Experience years is required")
    private String experienceYears;

    @NotBlank(message = "Phone is required")
    private String phone;
    
    @NotBlank(message = "Status is required")
    private String status;
}
