package com.hams.hospital_appointment_system.common.security;

import com.hams.hospital_appointment_system.common.security.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

        private static final String SECRET = "this-is-a-test-secret-key-with-enough-length-32bytes";

        private final JwtService jwtService = new JwtService(SECRET, 60_000);

        @Test
        void generateToken_shouldReturnNonNullToken() {

                String token = jwtService.generateToken("johndoe");

                assertThat(token).isNotNull();
                assertThat(token).isNotBlank();
        }

        @Test
        void extractUsername_shouldReturnUsername_whenTokenIsValid() {

                String token = jwtService.generateToken("johndoe");

                String username = jwtService.extractUsername(token);

                assertThat(username).isEqualTo("johndoe");
        }

        @Test
        void extractUsername_shouldThrowExpiredJwtException_whenTokenIsExpired() {

                JwtService expiredJwtService = new JwtService(SECRET, -1000);

                String token = expiredJwtService.generateToken("johndoe");

                assertThatThrownBy(() -> expiredJwtService.extractUsername(token))
                                .isInstanceOf(ExpiredJwtException.class);
        }

        @Test
        void extractUsername_shouldThrowException_whenTokenSignatureIsInvalid() {

                String token = jwtService.generateToken("johndoe");

                JwtService differentSecretJwtService =
                                new JwtService("a-completely-different-test-secret-key-with-64-bytes-of-length-total", 60_000);

                assertThatThrownBy(() -> differentSecretJwtService.extractUsername(token))
                                .isInstanceOf(SignatureException.class);
        }

        @Test
        void extractUsername_shouldThrowException_whenTokenIsMalformed() {

                assertThatThrownBy(() -> jwtService.extractUsername("not-a-valid-token"))
                                .isInstanceOf(io.jsonwebtoken.JwtException.class);
        }
}
