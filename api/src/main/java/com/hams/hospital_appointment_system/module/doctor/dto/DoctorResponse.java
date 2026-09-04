package com.hams.hospital_appointment_system.module.doctor.dto;

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
public class DoctorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String specialization;
    private String qualification;
    private String experienceYears;
    private String phone;
    private String status;
}
