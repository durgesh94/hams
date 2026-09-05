package com.hams.hospital_appointment_system.common.security;

import com.hams.hospital_appointment_system.common.security.filter.JwtAuthenticationFilter;
import com.hams.hospital_appointment_system.common.security.handler.JwtAuthenticationEntryPoint;
import com.hams.hospital_appointment_system.common.security.service.CustomUserDetailsService;
import com.hams.hospital_appointment_system.common.security.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

        @Mock
        private JwtService jwtService;

        @Mock
        private CustomUserDetailsService customUserDetailsService;

        @Mock
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        @Mock
        private HttpServletRequest request;

        @Mock
        private HttpServletResponse response;

        @Mock
        private FilterChain filterChain;

        @InjectMocks
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @AfterEach
        void tearDown() {
                SecurityContextHolder.clearContext();
        }

        @Test
        void doFilterInternal_shouldContinueChain_whenNoAuthorizationHeader() throws Exception {

                when(request.getHeader("Authorization")).thenReturn(null);

                jwtAuthenticationFilter.doFilter(request, response, filterChain);

                verify(filterChain).doFilter(request, response);
                assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
                verifyNoInteractions(jwtService, customUserDetailsService, jwtAuthenticationEntryPoint);
        }

        @Test
        void doFilterInternal_shouldContinueChain_whenAuthorizationHeaderIsNotBearer() throws Exception {

                when(request.getHeader("Authorization")).thenReturn("Basic abc123");

                jwtAuthenticationFilter.doFilter(request, response, filterChain);

                verify(filterChain).doFilter(request, response);
                assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
                verifyNoInteractions(jwtService, customUserDetailsService, jwtAuthenticationEntryPoint);
        }

        @Test
        void doFilterInternal_shouldSetAuthentication_whenJwtIsValid() throws Exception {

                UserDetails userDetails = User.builder()
                                .username("johndoe")
                                .password("secret")
                                .authorities(List.of(new SimpleGrantedAuthority("ROLE_PATIENT")))
                                .build();

                when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
                when(jwtService.extractUsername("valid-token")).thenReturn("johndoe");
                when(customUserDetailsService.loadUserByUsername("johndoe")).thenReturn(userDetails);

                jwtAuthenticationFilter.doFilter(request, response, filterChain);

                assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
                assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                                .isEqualTo(userDetails);
                verify(filterChain).doFilter(request, response);
                verifyNoInteractions(jwtAuthenticationEntryPoint);
        }

        @Test
        void doFilterInternal_shouldNotOverrideExistingAuthentication() throws Exception {

                UsernamePasswordAuthenticationToken existingAuth =
                                new UsernamePasswordAuthenticationToken("existingUser", null, List.of());
                SecurityContextHolder.getContext().setAuthentication(existingAuth);

                when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
                when(jwtService.extractUsername("valid-token")).thenReturn("johndoe");

                jwtAuthenticationFilter.doFilter(request, response, filterChain);

                assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(existingAuth);
                verify(filterChain).doFilter(request, response);
                verifyNoInteractions(customUserDetailsService);
        }

        @Test
        void doFilterInternal_shouldInvokeEntryPoint_whenJwtIsExpired() throws Exception {

                when(request.getHeader("Authorization")).thenReturn("Bearer expired-token");
                when(jwtService.extractUsername("expired-token"))
                                .thenThrow(mock(ExpiredJwtException.class));

                jwtAuthenticationFilter.doFilter(request, response, filterChain);

                ArgumentCaptor<BadCredentialsException> captor =
                                ArgumentCaptor.forClass(BadCredentialsException.class);
                verify(jwtAuthenticationEntryPoint)
                                .commence(eq(request), eq(response), captor.capture());
                assertThat(captor.getValue().getMessage())
                                .isEqualTo("JWT token has expired. Please authenticate again.");
                assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
                verify(filterChain, never()).doFilter(request, response);
        }

        @Test
        void doFilterInternal_shouldInvokeEntryPoint_whenJwtIsInvalid() throws Exception {

                when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
                when(jwtService.extractUsername("invalid-token"))
                                .thenThrow(new JwtException("malformed"));

                jwtAuthenticationFilter.doFilter(request, response, filterChain);

                ArgumentCaptor<BadCredentialsException> captor =
                                ArgumentCaptor.forClass(BadCredentialsException.class);
                verify(jwtAuthenticationEntryPoint)
                                .commence(eq(request), eq(response), captor.capture());
                assertThat(captor.getValue().getMessage())
                                .isEqualTo("Invalid JWT token. Please provide a valid JWT token.");
                assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
                verify(filterChain, never()).doFilter(request, response);
        }
}
