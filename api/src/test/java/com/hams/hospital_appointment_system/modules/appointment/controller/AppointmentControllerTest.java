package com.hams.hospital_appointment_system.modules.appointment.controller;

import com.hams.hospital_appointment_system.common.security.handler.JwtAuthenticationEntryPoint;
import com.hams.hospital_appointment_system.common.security.service.CustomUserDetailsService;
import com.hams.hospital_appointment_system.common.security.service.JwtService;
import com.hams.hospital_appointment_system.module.appointment.controller.AppointmentController;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentRequest;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentResponse;
import com.hams.hospital_appointment_system.module.appointment.entity.AppointmentStatus;
import com.hams.hospital_appointment_system.module.appointment.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AppointmentService appointmentService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private AppointmentRequest buildRequest() {
	return AppointmentRequest.builder()
		.patientId(2L)
		.doctorId(1L)
		.appointmentDate(LocalDate.now().plusDays(1))
		.appointmentTime(LocalTime.of(10, 30))
		.reason("Routine check-up")
		.notes("Bring previous reports")
		.build();
    }

    @Test
    @WithMockUser
    void createAppointment_shouldReturnCreated() throws Exception {
	when(appointmentService.createAppointment(any(AppointmentRequest.class)))
		.thenReturn(new AppointmentResponse());

	mockMvc.perform(post("/api/v1/appointments")
		.with(csrf())
		.contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(buildRequest())))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.status").value(201))
		.andExpect(jsonPath("$.message").value("Appointment created successfully"));

	verify(appointmentService).createAppointment(any(AppointmentRequest.class));
    }

    @Test
    @WithMockUser
    void getAppointmentById_shouldReturnOk() throws Exception {
	when(appointmentService.getAppointmentById(1L)).thenReturn(new AppointmentResponse());

	mockMvc.perform(get("/api/v1/appointments/{id}", 1L))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status").value(200))
		.andExpect(jsonPath("$.message").value("Appointment retrieved successfully"));

	verify(appointmentService).getAppointmentById(1L);
    }

    @Test
    @WithMockUser
    void updateAppointment_shouldReturnOk() throws Exception {
	when(appointmentService.updateAppointment(eq(1L), any(AppointmentRequest.class)))
		.thenReturn(new AppointmentResponse());

	mockMvc.perform(put("/api/v1/appointments/{id}", 1L)
		.with(csrf())
		.contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(buildRequest())))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status").value(200))
		.andExpect(jsonPath("$.message").value("Appointment updated successfully"));

	verify(appointmentService).updateAppointment(eq(1L), any(AppointmentRequest.class));
    }

    @Test
    @WithMockUser
    void updateAppointmentStatus_shouldReturnOk() throws Exception {
	when(appointmentService.updateAppointmentStatus(1L, AppointmentStatus.CONFIRMED))
		.thenReturn(new AppointmentResponse());

	mockMvc.perform(patch("/api/v1/appointments/{id}/status", 1L)
		.with(csrf())
		.param("status", "CONFIRMED"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status").value(200))
		.andExpect(jsonPath("$.message").value("Appointment status updated successfully"));

	verify(appointmentService).updateAppointmentStatus(1L, AppointmentStatus.CONFIRMED);
    }

    @Test
    @WithMockUser
    void getAppointments_shouldReturnOk() throws Exception {
	AppointmentResponse response = new AppointmentResponse();
	when(appointmentService.getAppointments(any(), any()))
		.thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));

	mockMvc.perform(get("/api/v1/appointments/filter")
		.param("doctorId", "1")
		.param("status", "BOOKED")
		.param("page", "0")
		.param("size", "10"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status").value(200))
		.andExpect(jsonPath("$.message").value("Appointments retrieved successfully"))
		.andExpect(jsonPath("$.data").isArray())
		.andExpect(jsonPath("$.data.length()").value(1))
		.andExpect(jsonPath("$.pagination.page").value(0))
		.andExpect(jsonPath("$.pagination.size").value(10))
		.andExpect(jsonPath("$.pagination.totalElements").value(1))
		.andExpect(jsonPath("$.pagination.totalPages").value(1))
		.andExpect(jsonPath("$.pagination.first").value(true))
		.andExpect(jsonPath("$.pagination.last").value(true));

	verify(appointmentService).getAppointments(any(), any());
    }

    @Test
    @WithMockUser
    void getAppointmentsList_shouldReturnOk() throws Exception {
	when(appointmentService.getAppointmentsList()).thenReturn(List.of(new AppointmentResponse()));

	mockMvc.perform(get("/api/v1/appointments"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status").value(200))
		.andExpect(jsonPath("$.message").value("Appointments retrieved successfully"))
		.andExpect(jsonPath("$.data").isArray())
		.andExpect(jsonPath("$.data.length()").value(1));

	verify(appointmentService).getAppointmentsList();
    }

    @Test
    @WithMockUser
    void deleteAppointment_shouldReturnNoContent() throws Exception {
	doNothing().when(appointmentService).deleteAppointment(1L);

	mockMvc.perform(delete("/api/v1/appointments/{id}", 1L)
		.with(csrf()))
		.andExpect(status().isNoContent())
		.andExpect(content().string(""));

	verify(appointmentService).deleteAppointment(1L);
    }
}
