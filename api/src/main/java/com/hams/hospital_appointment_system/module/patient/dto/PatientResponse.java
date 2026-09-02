package com.hams.hospital_appointment_system.module.patient.dto;

import java.time.LocalDate;

import com.hams.hospital_appointment_system.module.patient.entity.Gender;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
}
