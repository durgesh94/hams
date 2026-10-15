package com.hams.doctor.exception;

public class InvalidDoctorReviewStateException
        extends RuntimeException {

    public InvalidDoctorReviewStateException(String message) {
        super(message);
    }
}