package com.hams.hospital_appointment_system.modules.dashboard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hams.hospital_appointment_system.module.dashboard.dto.MonthlyDashboardResponse;
import com.hams.hospital_appointment_system.module.dashboard.repository.DashboardRepository;
import com.hams.hospital_appointment_system.module.dashboard.service.impl.DashboardServiceImpl;
import java.time.LocalDateTime;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {

  @Mock private DashboardRepository dashboardRepository;

  @InjectMocks private DashboardServiceImpl dashboardService;

  @Test
  void getMonthlyStatistics_shouldReturnCountsForRequestedMonth() {
    YearMonth month = YearMonth.of(2026, 9);
    LocalDateTime startDate = LocalDateTime.of(2026, 9, 1, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2026, 10, 1, 0, 0);

    when(dashboardRepository.countActiveDoctors()).thenReturn(12L);
    when(dashboardRepository.countNewPatients(startDate, endDate)).thenReturn(34L);
    when(dashboardRepository.countAppointments(startDate.toLocalDate(), endDate.toLocalDate()))
        .thenReturn(56L);

    MonthlyDashboardResponse response = dashboardService.getMonthlyStatistics(month);

    assertThat(response.getMonth()).isEqualTo("2026-09");
    assertThat(response.getActiveDoctorCount()).isEqualTo(12L);
    assertThat(response.getNewPatientCount()).isEqualTo(34L);
    assertThat(response.getAppointmentCount()).isEqualTo(56L);
    verify(dashboardRepository).countNewPatients(startDate, endDate);
    verify(dashboardRepository).countAppointments(startDate.toLocalDate(), endDate.toLocalDate());
  }

  @Test
  void getMonthlyStatistics_shouldReturnZeroCounts_whenNoActivityExists() {
    YearMonth month = YearMonth.of(2026, 2);
    LocalDateTime startDate = LocalDateTime.of(2026, 2, 1, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2026, 3, 1, 0, 0);

    when(dashboardRepository.countActiveDoctors()).thenReturn(0L);
    when(dashboardRepository.countNewPatients(eq(startDate), eq(endDate))).thenReturn(0L);
    when(dashboardRepository.countAppointments(
            eq(startDate.toLocalDate()), eq(endDate.toLocalDate())))
        .thenReturn(0L);

    MonthlyDashboardResponse response = dashboardService.getMonthlyStatistics(month);

    assertThat(response.getMonth()).isEqualTo("2026-02");
    assertThat(response.getActiveDoctorCount()).isZero();
    assertThat(response.getNewPatientCount()).isZero();
    assertThat(response.getAppointmentCount()).isZero();
    verify(dashboardRepository).countActiveDoctors();
  }
}
