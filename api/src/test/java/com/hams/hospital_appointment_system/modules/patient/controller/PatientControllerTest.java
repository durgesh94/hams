package com.hams.hospital_appointment_system.modules.patient.controller;

import com.hams.hospital_appointment_system.common.security.handler.JwtAuthenticationEntryPoint;
import com.hams.hospital_appointment_system.common.security.service.CustomUserDetailsService;
import com.hams.hospital_appointment_system.common.security.service.JwtService;
import com.hams.hospital_appointment_system.module.patient.controller.PatientController;
import com.hams.hospital_appointment_system.module.patient.dto.PatientRequest;
import com.hams.hospital_appointment_system.module.patient.dto.PatientResponse;
import com.hams.hospital_appointment_system.common.enums.Gender;
import com.hams.hospital_appointment_system.module.patient.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
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

@WebMvcTest(PatientController.class)
class PatientControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private PatientService patientService;

        @MockitoBean
        private JwtService jwtService;

        @MockitoBean
        private CustomUserDetailsService customUserDetailsService;

        @MockitoBean
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        @Test
        @WithMockUser
        void createPatient_shouldReturnCreated() throws Exception {

                PatientRequest request = new PatientRequest();
                request.setFirstName("John");
                request.setLastName("Doe");
                request.setDateOfBirth(LocalDate.of(1990, 1, 1));
                request.setGender(Gender.valueOf("MALE"));
                request.setEmail("john.doe@test.com");
                request.setPhone("9876543210");

                PatientResponse response = new PatientResponse();

                when(patientService.createPatient(any(PatientRequest.class)))
                                .thenReturn(response);

                mockMvc.perform(post("/api/v1/patients")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.status").value(201))
                                .andExpect(jsonPath("$.message")
                                                .value("Patient created successfully"));

                verify(patientService).createPatient(any(PatientRequest.class));
        }

        @Test
        @WithMockUser
        void getAllPatients_shouldReturnOk() throws Exception {

                PatientResponse response1 = new PatientResponse();
                PatientResponse response2 = new PatientResponse();

                when(patientService.getAllPatients())
                                .thenReturn(List.of(response1, response2));

                mockMvc.perform(get("/api/v1/patients"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.message")
                                                .value("Fetched all patients"))
                                .andExpect(jsonPath("$.data").isArray())
                                .andExpect(jsonPath("$.data.length()").value(2));

                verify(patientService).getAllPatients();
        }

        @Test
        @WithMockUser
        void getAllPatients_shouldReturnEmptyList() throws Exception {

                when(patientService.getAllPatients())
                                .thenReturn(List.of());

                mockMvc.perform(get("/api/v1/patients"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.data").isArray())
                                .andExpect(jsonPath("$.data.length()").value(0));

                verify(patientService).getAllPatients();
        }

        @Test
        @WithMockUser
        void getPatientById_shouldReturnOk() throws Exception {

                Long patientId = 1L;

                PatientResponse response = new PatientResponse();

                when(patientService.getPatientById(patientId))
                                .thenReturn(response);

                mockMvc.perform(get("/api/v1/patients/{id}", patientId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.message")
                                                .value("Patient fetched successfully"));

                verify(patientService).getPatientById(patientId);
        }

        @Test
        @WithMockUser
        void updatePatientById_shouldReturnOk() throws Exception {

                Long patientId = 1L;

                PatientRequest request = new PatientRequest();
                request.setFirstName("John");
                request.setLastName("Updated");
                request.setDateOfBirth(LocalDate.of(1990, 1, 1));
                request.setGender(Gender.valueOf("MALE"));
                request.setEmail("john.updated@test.com");
                request.setPhone("9876543210");

                PatientResponse response = new PatientResponse();

                when(patientService.updatePatientById(
                                eq(patientId),
                                any(PatientRequest.class))).thenReturn(response);

                mockMvc.perform(put("/api/v1/patients/{id}", patientId)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.message")
                                                .value("Patient updated successfully"));

                verify(patientService).updatePatientById(
                                eq(patientId),
                                any(PatientRequest.class));
        }

        @Test
        @WithMockUser
        void deletePatientById_shouldReturnNoContent() throws Exception {

                Long patientId = 1L;

                when(patientService.deletePatientById(patientId))
                                .thenReturn(new PatientResponse());

                mockMvc.perform(delete("/api/v1/patients/{id}", patientId)
                                .with(csrf()))
                                .andExpect(status().isNoContent())
                                .andExpect(content().string(""));

                verify(patientService).deletePatientById(patientId);
        }
}