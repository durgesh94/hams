package com.hams.hospital_appointment_system.module.patient.dto;

import com.hams.hospital_appointment_system.common.enums.Gender;
import java.time.LocalDate;
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
