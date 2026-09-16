package com.hams.hospital_appointment_system.modules.dashboard.controller;

import com.hams.hospital_appointment_system.common.security.handler.JwtAuthenticationEntryPoint;
import com.hams.hospital_appointment_system.common.security.service.CustomUserDetailsService;
import com.hams.hospital_appointment_system.common.security.service.JwtService;
import com.hams.hospital_appointment_system.module.dashboard.controller.DashboardController;
import com.hams.hospital_appointment_system.module.dashboard.dto.MonthlyDashboardResponse;
import com.hams.hospital_appointment_system.module.dashboard.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.YearMonth;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardController.class)
public class DashboardControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private DashboardService dashboardService;

	@MockitoBean
	private JwtService jwtService;

	@MockitoBean
	private CustomUserDetailsService customUserDetailsService;

	@MockitoBean
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Test
	@WithMockUser
	void getMonthlyStatistics_shouldReturnOk() throws Exception {
		YearMonth month = YearMonth.of(2026, 9);
		MonthlyDashboardResponse response = MonthlyDashboardResponse.builder()
				.month("2026-09")
				.activeDoctorCount(12L)
				.newPatientCount(34L)
				.appointmentCount(56L)
				.build();

		when(dashboardService.getMonthlyStatistics(month)).thenReturn(response);

		mockMvc.perform(get("/api/v1/dashboard/month")
				.param("month", "2026-09"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value(200))
				.andExpect(jsonPath("$.message").value("Monthly statistics retrieved successfully"))
				.andExpect(jsonPath("$.data.month").value("2026-09"))
				.andExpect(jsonPath("$.data.activeDoctorCount").value(12))
				.andExpect(jsonPath("$.data.newPatientCount").value(34))
				.andExpect(jsonPath("$.data.appointmentCount").value(56));

		verify(dashboardService).getMonthlyStatistics(month);
	}

	@Test
	@WithMockUser
	void getMonthlyStatistics_shouldReturnServerError_whenMonthIsInvalid() throws Exception {
		mockMvc.perform(get("/api/v1/dashboard/month")
				.param("month", "2026-13"))
				.andExpect(status().isInternalServerError());

		verify(dashboardService, never()).getMonthlyStatistics(org.mockito.ArgumentMatchers.any());
	}
}
