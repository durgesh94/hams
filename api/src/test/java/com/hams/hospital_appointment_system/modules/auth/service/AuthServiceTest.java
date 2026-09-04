package com.hams.hospital_appointment_system.modules.auth.service;

import com.hams.hospital_appointment_system.common.security.service.JwtService;
import com.hams.hospital_appointment_system.module.auth.dto.LoginRequest;
import com.hams.hospital_appointment_system.module.auth.dto.LoginResponse;
import com.hams.hospital_appointment_system.module.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

        @Mock
        private AuthenticationManager authenticationManager;

        @Mock
        private JwtService jwtService;

        @InjectMocks
        private AuthService authService;

        @Test
        void login_shouldReturnToken_whenCredentialsAreValid() {

                LoginRequest request = LoginRequest.builder()
                                .username("johndoe")
                                .password("secret")
                                .build();

                when(jwtService.generateToken("johndoe")).thenReturn("jwt-token");

                LoginResponse response = authService.login(request);

                assertThat(response.getToken()).isEqualTo("jwt-token");

                ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
                                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
                verify(authenticationManager).authenticate(captor.capture());
                assertThat(captor.getValue().getPrincipal()).isEqualTo("johndoe");
                assertThat(captor.getValue().getCredentials()).isEqualTo("secret");

                verify(jwtService).generateToken("johndoe");
        }

        @Test
        void login_shouldThrowException_whenCredentialsAreInvalid() {

                LoginRequest request = LoginRequest.builder()
                                .username("johndoe")
                                .password("wrong-password")
                                .build();

                when(authenticationManager.authenticate(any()))
                                .thenThrow(new BadCredentialsException("Bad credentials"));

                assertThatThrownBy(() -> authService.login(request))
                                .isInstanceOf(BadCredentialsException.class)
                                .hasMessage("Bad credentials");

                verify(jwtService, org.mockito.Mockito.never()).generateToken(any());
        }
}
