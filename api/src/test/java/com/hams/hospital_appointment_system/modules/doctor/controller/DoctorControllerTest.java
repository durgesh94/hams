package com.hams.hospital_appointment_system.modules.doctor.controller;

import com.hams.hospital_appointment_system.common.enums.Gender;
import com.hams.hospital_appointment_system.common.security.handler.JwtAuthenticationEntryPoint;
import com.hams.hospital_appointment_system.common.security.service.CustomUserDetailsService;
import com.hams.hospital_appointment_system.common.security.service.JwtService;
import com.hams.hospital_appointment_system.module.doctor.controller.DoctorController;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorRequest;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorResponse;
import com.hams.hospital_appointment_system.module.doctor.entity.DoctorStatus;
import com.hams.hospital_appointment_system.module.doctor.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
class DoctorControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private DoctorService doctorService;

        @MockitoBean
        private JwtService jwtService;

        @MockitoBean
        private CustomUserDetailsService customUserDetailsService;

        @MockitoBean
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        private DoctorRequest buildRequest() {
                return DoctorRequest.builder()
                                .firstName("John")
                                .lastName("Smith")
                                .gender(Gender.MALE)
                                .email("john.smith@test.com")
                                .specialization("Cardiology")
                                .qualification("MBBS, MD")
                                .experienceYears(10)
                                .phone("9876543210")
                                .status(DoctorStatus.ACTIVE)
                                .build();
        }

        @Test
        @WithMockUser
        void getAllDoctors_shouldReturnOk() throws Exception {

                DoctorResponse response1 = new DoctorResponse();
                DoctorResponse response2 = new DoctorResponse();

                when(doctorService.getAllDoctors())
                                .thenReturn(List.of(response1, response2));

                mockMvc.perform(get("/api/v1/doctors"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.message")
                                                .value("Fetched all doctors successfully."))
                                .andExpect(jsonPath("$.data").isArray())
                                .andExpect(jsonPath("$.data.length()").value(2));

                verify(doctorService).getAllDoctors();
        }

        @Test
        @WithMockUser
        void getAllDoctors_shouldReturnEmptyList() throws Exception {

                when(doctorService.getAllDoctors())
                                .thenReturn(List.of());

                mockMvc.perform(get("/api/v1/doctors"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data").isArray())
                                .andExpect(jsonPath("$.data.length()").value(0));

                verify(doctorService).getAllDoctors();
        }

        @Test
        @WithMockUser
        void createDoctor_shouldReturnOk() throws Exception {

                DoctorRequest request = buildRequest();
                DoctorResponse response = new DoctorResponse();

                when(doctorService.createDoctor(any(DoctorRequest.class)))
                                .thenReturn(response);

                mockMvc.perform(post("/api/v1/doctors")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.message")
                                                .value("Doctor created successfully."));

                verify(doctorService).createDoctor(any(DoctorRequest.class));
        }

        @Test
        @WithMockUser
        void getDoctorById_shouldReturnOk() throws Exception {

                Long doctorId = 1L;
                DoctorResponse response = new DoctorResponse();

                when(doctorService.getDoctorById(doctorId))
                                .thenReturn(response);

                mockMvc.perform(get("/api/v1/doctors/{id}", doctorId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.message")
                                                .value("Fetched doctor successfully."));

                verify(doctorService).getDoctorById(doctorId);
        }

        @Test
        @WithMockUser
        void updateDoctor_shouldReturnOk() throws Exception {

                Long doctorId = 1L;
                DoctorRequest request = buildRequest();
                DoctorResponse response = new DoctorResponse();

                when(doctorService.updateDoctor(eq(doctorId), any(DoctorRequest.class)))
                                .thenReturn(response);

                mockMvc.perform(put("/api/v1/doctors/{id}", doctorId)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.message")
                                                .value("Doctor updated successfully."));

                verify(doctorService).updateDoctor(eq(doctorId), any(DoctorRequest.class));
        }

        @Test
        @WithMockUser
        void deleteDoctor_shouldReturnOk() throws Exception {

                Long doctorId = 1L;

                doNothing().when(doctorService).deleteDoctor(doctorId);

                mockMvc.perform(delete("/api/v1/doctors/{id}", doctorId)
                                .with(csrf()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.message")
                                                .value("Doctor deleted successfully."));

                verify(doctorService).deleteDoctor(doctorId);
        }

        @Test
        @WithMockUser
        void getByFilter_shouldReturnOk() throws Exception {

                DoctorResponse response = new DoctorResponse();

                when(doctorService.getDoctorsByFilter(any()))
                                .thenReturn(List.of(response));

                mockMvc.perform(get("/api/v1/doctors/filter")
                                .param("specialization", "Cardiology"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.message")
                                                .value("Fetched doctors by filter successfully."))
                                .andExpect(jsonPath("$.data").isArray())
                                .andExpect(jsonPath("$.data.length()").value(1));

                verify(doctorService).getDoctorsByFilter(any());
        }
}
