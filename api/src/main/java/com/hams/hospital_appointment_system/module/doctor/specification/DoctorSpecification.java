package com.hams.hospital_appointment_system.module.doctor.specification;

import org.springframework.data.jpa.domain.Specification;
import com.hams.hospital_appointment_system.module.doctor.entity.Doctor;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorFilterRequest;

public class DoctorSpecification {

    private DoctorSpecification() {
        // Private constructor to prevent instantiation
    }

    public static Specification<Doctor> byFilter(DoctorFilterRequest filterRequest) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (filterRequest.getGender() != null) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(
                                root.get("gender"),
                                filterRequest.getGender()));
            }
            if (filterRequest.getSpecialization() != null) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("specialization")),
                                filterRequest.getSpecialization().toLowerCase()));
            }
            if (filterRequest.getStatus() != null) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(
                                root.get("status"),
                                filterRequest.getStatus()));
            }

            return predicates;
        };
    }

}
