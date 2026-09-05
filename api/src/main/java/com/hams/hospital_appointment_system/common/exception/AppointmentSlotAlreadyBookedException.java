package com.hams.hospital_appointment_system.common.exception;

public class AppointmentSlotAlreadyBookedException extends RuntimeException {

    public AppointmentSlotAlreadyBookedException(String message) {
        super(message);
    }
}