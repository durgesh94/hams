package com.hams.doctor.client.dto;

import java.util.UUID;

public record AuthUserResponse(
        UUID id,
        String username,
        String role
) {
}