package com.hams.auth.dto.internal;

public record ServiceTokenResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}