
package com.hams.doctor.dto;

import java.time.Instant;

public record ApiErrorResponse(
    String code,
    String message,
    int status,
    Instant timestamp
) {}
