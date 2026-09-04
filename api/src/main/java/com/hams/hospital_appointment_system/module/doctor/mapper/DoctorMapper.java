package com.hams.hospital_appointment_system.module.doctor.mapper;

import com.hams.hospital_appointment_system.module.doctor.dto.DoctorRequest;
import com.hams.hospital_appointment_system.module.doctor.entity.Doctor;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorResponse;

public class DoctorMapper {

    private DoctorMapper() {
        // Private constructor to prevent instantiation
    }

    public static Doctor toEntity(DoctorRequest doctorRequest) {
        return Doctor.builder()
                // Map fields from DoctorRequest to Doctor entity
                .firstName(doctorRequest.getFirstName())
                .lastName(doctorRequest.getLastName())
                .gender(doctorRequest.getGender())
                .qualification(doctorRequest.getQualification())
                .specialization(doctorRequest.getSpecialization())
                .experienceYears(doctorRequest.getExperienceYears())
                .email(doctorRequest.getEmail())
                .phone(doctorRequest.getPhone())
                .status(doctorRequest.getStatus())
                .build();
    }

    public static DoctorResponse toDto(Doctor doctor) {
        return DoctorResponse.builder()
                // Map fields from Doctor entity to DoctorResponse
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .gender(doctor.getGender().name())
                .qualification(doctor.getQualification())
                .specialization(doctor.getSpecialization())
                .experienceYears(doctor.getExperienceYears())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .status(doctor.getStatus().name())
                .build();
    }

    public static Doctor updateEntity(Doctor doctor, DoctorRequest doctorRequest) {
        doctor.setFirstName(doctorRequest.getFirstName());
        doctor.setLastName(doctorRequest.getLastName());
        doctor.setGender(doctorRequest.getGender());
        doctor.setQualification(doctorRequest.getQualification());
        doctor.setSpecialization(doctorRequest.getSpecialization());
        doctor.setExperienceYears(doctorRequest.getExperienceYears());
        doctor.setEmail(doctorRequest.getEmail());
        doctor.setPhone(doctorRequest.getPhone());
        doctor.setStatus(doctorRequest.getStatus());
        return doctor;
    }
}
