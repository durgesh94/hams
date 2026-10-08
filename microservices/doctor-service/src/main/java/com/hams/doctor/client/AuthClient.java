package com.hams.doctor.client;

import com.hams.doctor.client.dto.AuthUserResponse;
import com.hams.doctor.client.dto.CreateUserRequest;
import com.hams.doctor.config.FeignAuthInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "auth-service",
        url = "${services.auth.url}",
        configuration = FeignAuthInterceptor .class
)
public interface AuthClient {

    @PostMapping("/api/v1/internal/users")
    AuthUserResponse createUser(
            @RequestBody CreateUserRequest request
    );
}