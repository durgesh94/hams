package com.hams.doctor.client;

import com.hams.doctor.client.dto.ServiceTokenRequest;
import com.hams.doctor.client.dto.ServiceTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "auth-service-token",
        url = "${services.auth.url}"
)
public interface ServiceAuthClient {

    @PostMapping("/api/v1/internal/auth/token")
    ServiceTokenResponse createToken(
            @RequestBody ServiceTokenRequest request
    );
}