package com.hams.hospital_appointment_system.modules.user.controller;

import com.hams.hospital_appointment_system.common.security.handler.JwtAuthenticationEntryPoint;
import com.hams.hospital_appointment_system.common.security.service.CustomUserDetailsService;
import com.hams.hospital_appointment_system.common.security.service.JwtService;
import com.hams.hospital_appointment_system.module.user.controller.UserController;
import com.hams.hospital_appointment_system.module.user.dto.UserResponse;
import com.hams.hospital_appointment_system.module.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private UserService userService;

        @MockitoBean
        private JwtService jwtService;

        @MockitoBean
        private CustomUserDetailsService customUserDetailsService;

        @MockitoBean
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        @Test
        @WithMockUser
        void getAllUsers_shouldReturnOk() throws Exception {

                UserResponse response1 = new UserResponse();
                UserResponse response2 = new UserResponse();

                when(userService.getAllUsers())
                                .thenReturn(List.of(response1, response2));

                mockMvc.perform(get("/api/v1/users"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.message")
                                                .value("Fetched all users successfully"))
                                .andExpect(jsonPath("$.data").isArray())
                                .andExpect(jsonPath("$.data.length()").value(2));

                verify(userService).getAllUsers();
        }

        @Test
        @WithMockUser
        void getAllUsers_shouldReturnEmptyList() throws Exception {

                when(userService.getAllUsers())
                                .thenReturn(List.of());

                mockMvc.perform(get("/api/v1/users"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data").isArray())
                                .andExpect(jsonPath("$.data.length()").value(0));

                verify(userService).getAllUsers();
        }

        @Test
        @WithMockUser
        void getUserById_shouldReturnOk() throws Exception {

                Long userId = 1L;
                UserResponse response = new UserResponse();

                when(userService.getUserById(userId))
                                .thenReturn(response);

                mockMvc.perform(get("/api/v1/users/id")
                                .param("id", String.valueOf(userId)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.message")
                                                .value("Fetched user successfully"));

                verify(userService).getUserById(userId);
        }

        @Test
        @WithMockUser
        void getUserByUsername_shouldReturnOk() throws Exception {

                String username = "johndoe";
                UserResponse response = new UserResponse();

                when(userService.getUserByUsername(username))
                                .thenReturn(response);

                mockMvc.perform(get("/api/v1/users/username")
                                .param("username", username))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.message")
                                                .value("Fetched user successfully"));

                verify(userService).getUserByUsername(username);
        }
}
