package com.hams.hospital_appointment_system.module.dashboard.service;

import com.hams.hospital_appointment_system.module.dashboard.dto.MonthlyDashboardResponse;

import java.time.YearMonth;

public interface DashboardService {

    MonthlyDashboardResponse getMonthlyStatistics(YearMonth month);
}