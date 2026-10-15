package com.hams.doctor.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DoctorReviewRequest(

        @NotNull ReviewDecision decision,

        @Size(max = 500) String remarks

) {
    public enum ReviewDecision {
        APPROVED,
        REJECTED
    }
}