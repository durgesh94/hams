package com.hams.doctor.client;

import com.hams.doctor.client.dto.AuthUserResponse;
import com.hams.doctor.client.dto.CreateUserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "auth-service",
        url = "${services.auth.url}"
)
public interface AuthClient {

    @PostMapping("/api/v1/internal/users")
    AuthUserResponse createUser(
            @RequestBody CreateUserRequest request
    );
}