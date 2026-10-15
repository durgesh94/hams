package com.hams.auth.dto.internal;

import jakarta.validation.constraints.NotBlank;

public record ServiceTokenRequest(

        @NotBlank(message = "Service name is required")
        String serviceName,

        @NotBlank(message = "Service secret is required")
        String serviceSecret
) {
}