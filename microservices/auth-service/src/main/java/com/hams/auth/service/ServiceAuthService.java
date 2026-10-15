package com.hams.auth.service;

import com.hams.auth.dto.internal.ServiceTokenRequest;
import com.hams.auth.dto.internal.ServiceTokenResponse;
import com.hams.auth.exception.InvalidCredentialsException;
import com.hams.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceAuthService {

    private final JwtService jwtService;

    @Value("${services.doctor.name}")
    private String doctorServiceName;

    @Value("${services.doctor.secret}")
    private String doctorServiceSecret;

    public ServiceTokenResponse createServiceToken(
            ServiceTokenRequest request
    ) {

        if (!doctorServiceName.equals(request.serviceName())
                || !doctorServiceSecret.equals(request.serviceSecret())) {

            throw new InvalidCredentialsException(
                    "Invalid service credentials"
            );
        }

        String token = jwtService.generateServiceToken(
                request.serviceName()
        );

        return new ServiceTokenResponse(
                token,
                "Bearer",
                jwtService.getExpiration()
        );
    }
}