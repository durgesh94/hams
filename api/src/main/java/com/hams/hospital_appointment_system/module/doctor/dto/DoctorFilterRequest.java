package com.hams.hospital_appointment_system.module.doctor.dto;
import com.hams.hospital_appointment_system.common.enums.Gender;
import com.hams.hospital_appointment_system.module.doctor.entity.DoctorStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorFilterRequest {
    
    private Gender gender;

    private String specialization;
    
    private DoctorStatus status;
}
