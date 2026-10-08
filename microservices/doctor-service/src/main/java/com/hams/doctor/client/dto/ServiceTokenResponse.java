package com.hams.doctor.client.dto;

public record ServiceTokenResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}