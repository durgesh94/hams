package com.hams.auth.dto.internal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateInternalUserRequest(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 100)
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100)
        String password,

        @NotBlank(message = "Role is required")
        String role
) {
}