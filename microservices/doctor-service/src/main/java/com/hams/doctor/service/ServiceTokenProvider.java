package com.hams.doctor.service;

import com.hams.doctor.client.ServiceAuthClient;
import com.hams.doctor.client.dto.ServiceTokenRequest;
import com.hams.doctor.client.dto.ServiceTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ServiceTokenProvider {

    private final ServiceAuthClient serviceAuthClient;

    @Value("${services.auth.name}")
    private String serviceName;

    @Value("${services.auth.secret}")
    private String serviceSecret;

    private String accessToken;
    private Instant tokenExpiresAt;

    public synchronized String getToken() {

        if (isTokenExpiredOrExpiringSoon()) {
            requestNewToken();
        }

        return accessToken;
    }

    private boolean isTokenExpiredOrExpiringSoon() {

        if (accessToken == null || tokenExpiresAt == null) {
            return true;
        }

        return Instant.now()
                .isAfter(tokenExpiresAt.minusSeconds(30));
    }

    private void requestNewToken() {

        ServiceTokenRequest request =
                new ServiceTokenRequest(
                        serviceName,
                        serviceSecret
                );

        ServiceTokenResponse response =
                serviceAuthClient.createToken(request);

        accessToken = response.accessToken();

        tokenExpiresAt = Instant.now()
                .plusMillis(response.expiresIn());
    }
}