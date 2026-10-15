package com.hams.doctor.client.dto;

public record CreateUserRequest(
        String username,
        String password,
        String role
) {
}