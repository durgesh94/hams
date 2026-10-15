package com.hams.doctor.client.dto;

public record ServiceTokenRequest(
        String serviceName,
        String serviceSecret
) {
}