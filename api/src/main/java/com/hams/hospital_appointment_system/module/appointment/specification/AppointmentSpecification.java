package com.hams.hospital_appointment_system.module.appointment.specification;

import org.springframework.data.jpa.domain.Specification;

import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentFilter;
import com.hams.hospital_appointment_system.module.appointment.entity.Appointment;

public class AppointmentSpecification {

    private AppointmentSpecification() {
        // Private constructor to prevent instantiation of this utility class
    }

    public static Specification<Appointment> filter(AppointmentFilter filter) {

        return (root, query, criteriaBuilder) -> {

            var predicates = criteriaBuilder.conjunction();

            if (filter.getDoctorId() != null) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(
                                root.get("doctor").get("id"),
                                filter.getDoctorId()));
            }

            if (filter.getPatientId() != null) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(
                                root.get("patient").get("id"),
                                filter.getPatientId()));
            }

            if (filter.getDate() != null) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(
                                root.get("appointmentDate"),
                                filter.getDate()));
            }

            if (filter.getStatus() != null) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(
                                root.get("status"),
                                filter.getStatus()));
            }

            return predicates;
        };
    }
}
