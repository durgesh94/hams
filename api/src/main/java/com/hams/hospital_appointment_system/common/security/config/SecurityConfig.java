package com.hams.hospital_appointment_system.common.security.config;

import com.hams.hospital_appointment_system.common.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final AuthenticationEntryPoint authenticationEntryPoint;
        private final AccessDeniedHandler accessDeniedHandler;

        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                        AuthenticationEntryPoint authenticationEntryPoint,
                        AccessDeniedHandler accessDeniedHandler) {
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
                this.authenticationEntryPoint = authenticationEntryPoint;
                this.accessDeniedHandler = accessDeniedHandler;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                return httpSecurity
                                .csrf(csrf -> csrf.disable())

                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(authenticationEntryPoint)
                                                .accessDeniedHandler(accessDeniedHandler))

                                .authorizeHttpRequests(auth -> auth
                                                // Public endpoints
                                                .requestMatchers(
                                                                "/api/v1/auth/login",
                                                                "/api/v1/health")
                                                .permitAll()

                                                // Both ADMIN and OPERATOR can view data
                                                .requestMatchers(HttpMethod.GET, "/api/v1/**")
                                                .hasAnyRole("ADMIN", "OPERATOR")

                                                // Only ADMIN can modify data
                                                .requestMatchers(HttpMethod.POST, "/api/v1/**")
                                                .hasRole("ADMIN")

                                                .requestMatchers(HttpMethod.PUT, "/api/v1/**")
                                                .hasRole("ADMIN")

                                                .requestMatchers(HttpMethod.DELETE, "/api/v1/**")
                                                .hasRole("ADMIN")

                                                // Anything else requires authentication
                                                .anyRequest()
                                                .authenticated())

                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class)

                                .build();
        }
}
