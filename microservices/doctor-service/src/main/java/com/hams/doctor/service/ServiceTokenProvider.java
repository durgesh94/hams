package com.hams.doctor.service;

import com.hams.doctor.client.ServiceAuthClient;
import com.hams.doctor.client.dto.ServiceTokenRequest;
import com.hams.doctor.client.dto.ServiceTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceTokenProvider {

    private final ServiceAuthClient serviceAuthClient;

    @Value("${services.auth.name}")
    private String serviceName;

    @Value("${services.auth.secret}")
    private String serviceSecret;

    private String accessToken;

    public String getToken() {

        if (accessToken == null) {
            accessToken = requestNewToken();
        }

        return accessToken;
    }

    private String requestNewToken() {

        ServiceTokenRequest request =
                new ServiceTokenRequest(
                        serviceName,
                        serviceSecret
                );

        ServiceTokenResponse response =
                serviceAuthClient.createToken(request);

        return response.accessToken();
    }
}