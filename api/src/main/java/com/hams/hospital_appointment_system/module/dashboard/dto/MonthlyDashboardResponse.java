package com.hams.hospital_appointment_system.module.dashboard.dto;

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
public class MonthlyDashboardResponse {
   
    private String month;
    private Long activeDoctorCount;
    private Long newPatientCount;
    private Long appointmentCount;
}
