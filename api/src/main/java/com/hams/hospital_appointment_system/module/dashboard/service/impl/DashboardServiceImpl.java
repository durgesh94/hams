package com.hams.hospital_appointment_system.module.dashboard.service.impl;

import com.hams.hospital_appointment_system.module.dashboard.service.DashboardService;

import lombok.RequiredArgsConstructor;

import com.hams.hospital_appointment_system.module.dashboard.dto.MonthlyDashboardResponse;
import com.hams.hospital_appointment_system.module.dashboard.repository.DashboardRepository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;

    @Override
    public MonthlyDashboardResponse getMonthlyStatistics(YearMonth month) {

        LocalDateTime startDate = month
                .atDay(1)
                .atStartOfDay();

        LocalDateTime endDate = month
                .plusMonths(1)
                .atDay(1)
                .atStartOfDay();

        long activeDoctorCount = dashboardRepository.countActiveDoctors();

        long newPatientCount = dashboardRepository.countNewPatients(
                startDate,
                endDate);

        long appointmentCount = dashboardRepository.countAppointments(
                startDate,
                endDate);

        return MonthlyDashboardResponse.builder()
                .month(month.toString())
                .activeDoctorCount(activeDoctorCount)
                .newPatientCount(newPatientCount)
                .appointmentCount(appointmentCount)
                .build();
    }
}
