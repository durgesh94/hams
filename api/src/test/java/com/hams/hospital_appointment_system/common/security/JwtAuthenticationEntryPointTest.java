package com.hams.hospital_appointment_system.common.security;

import com.hams.hospital_appointment_system.common.security.handler.JwtAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import tools.jackson.databind.ObjectMapper;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtAuthenticationEntryPointTest {

        private final ObjectMapper objectMapper = new ObjectMapper();

        private final JwtAuthenticationEntryPoint entryPoint =
                        new JwtAuthenticationEntryPoint(objectMapper);

        @Test
        void commence_shouldWriteUnauthorizedResponse_withExceptionMessage() throws Exception {

                HttpServletRequest request = mock(HttpServletRequest.class);
                HttpServletResponse response = mock(HttpServletResponse.class);
                StringWriter stringWriter = new StringWriter();
                when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

                AuthenticationException exception = new BadCredentialsException("Invalid JWT token.");

                entryPoint.commence(request, response, exception);

                verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
                verify(response).setContentType("application/json");

                String body = stringWriter.toString();
                assertThat(body).contains("\"status\":401");
                assertThat(body).contains("Invalid JWT token.");
        }

        @Test
        void commence_shouldUseDefaultMessage_whenExceptionMessageIsBlank() throws Exception {

                HttpServletRequest request = mock(HttpServletRequest.class);
                HttpServletResponse response = mock(HttpServletResponse.class);
                StringWriter stringWriter = new StringWriter();
                when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

                AuthenticationException exception = new BadCredentialsException("");

                entryPoint.commence(request, response, exception);

                String body = stringWriter.toString();
                assertThat(body).contains("Authentication required. Please provide a valid JWT token.");
        }

        @Test
        void commence_shouldUseDefaultMessage_whenExceptionMessageIsNull() throws Exception {

                HttpServletRequest request = mock(HttpServletRequest.class);
                HttpServletResponse response = mock(HttpServletResponse.class);
                StringWriter stringWriter = new StringWriter();
                when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

                AuthenticationException exception = new BadCredentialsException(null);

                entryPoint.commence(request, response, exception);

                String body = stringWriter.toString();
                assertThat(body).contains("Authentication required. Please provide a valid JWT token.");
        }
}
