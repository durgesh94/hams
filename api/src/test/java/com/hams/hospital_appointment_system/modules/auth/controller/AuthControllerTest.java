package com.hams.hospital_appointment_system.modules.auth.controller;

import com.hams.hospital_appointment_system.common.security.handler.JwtAuthenticationEntryPoint;
import com.hams.hospital_appointment_system.common.security.service.CustomUserDetailsService;
import com.hams.hospital_appointment_system.common.security.service.JwtService;
import com.hams.hospital_appointment_system.module.auth.controller.AuthController;
import com.hams.hospital_appointment_system.module.auth.dto.LoginRequest;
import com.hams.hospital_appointment_system.module.auth.dto.LoginResponse;
import com.hams.hospital_appointment_system.module.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private AuthService authService;

        @MockitoBean
        private JwtService jwtService;

        @MockitoBean
        private CustomUserDetailsService customUserDetailsService;

        @MockitoBean
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        @Test
        @WithMockUser
        void login_shouldReturnOk_whenCredentialsAreValid() throws Exception {

                LoginRequest request = LoginRequest.builder()
                                .username("johndoe")
                                .password("secret")
                                .build();

                LoginResponse response = LoginResponse.builder()
                                .token("jwt-token")
                                .build();

                when(authService.login(any(LoginRequest.class)))
                                .thenReturn(response);

                mockMvc.perform(post("/api/v1/auth/login")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").value("jwt-token"));

                verify(authService).login(any(LoginRequest.class));
        }

        @Test
        @WithMockUser
        void login_shouldReturnBadRequest_whenUsernameIsBlank() throws Exception {

                LoginRequest request = LoginRequest.builder()
                                .username("")
                                .password("secret")
                                .build();

                mockMvc.perform(post("/api/v1/auth/login")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void login_shouldReturnBadRequest_whenPasswordIsBlank() throws Exception {

                LoginRequest request = LoginRequest.builder()
                                .username("johndoe")
                                .password("")
                                .build();

                mockMvc.perform(post("/api/v1/auth/login")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }
}
