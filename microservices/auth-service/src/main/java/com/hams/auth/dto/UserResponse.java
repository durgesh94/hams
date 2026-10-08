package com.hams.auth.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String role
) {
}