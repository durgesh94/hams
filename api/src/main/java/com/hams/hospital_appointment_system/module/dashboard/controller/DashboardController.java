package com.hams.hospital_appointment_system.module.dashboard.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.YearMonth;

import com.hams.hospital_appointment_system.common.response.ApiResponse;
import com.hams.hospital_appointment_system.module.dashboard.dto.MonthlyDashboardResponse;
import com.hams.hospital_appointment_system.module.dashboard.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/month")
    public ResponseEntity<ApiResponse<MonthlyDashboardResponse>> getMonthlyStatistics(@RequestParam YearMonth month) {
        MonthlyDashboardResponse monthlyDashboardResponse = dashboardService.getMonthlyStatistics(month);
        ApiResponse<MonthlyDashboardResponse> apiResponse = ApiResponse.<MonthlyDashboardResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Monthly statistics retrieved successfully")
                .data(monthlyDashboardResponse)
                .build();
        return ResponseEntity.ok(apiResponse);

    }
}
