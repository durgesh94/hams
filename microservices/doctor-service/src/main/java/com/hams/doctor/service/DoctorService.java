package com.hams.doctor.service;

import com.hams.doctor.dto.CreateDoctorRequest;
import com.hams.doctor.dto.DoctorResponse;
import com.hams.doctor.dto.UpdateDoctorRequest;
import com.hams.doctor.entity.Doctor;
import com.hams.doctor.entity.DoctorStatus;
import com.hams.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    @Transactional
    public DoctorResponse createDoctor(CreateDoctorRequest request) {

        if (request.userId() != null
                && doctorRepository.existsByUserId(request.userId())) {

            throw new IllegalArgumentException(
                    "Doctor profile already exists for this user"
            );
        }

        Doctor doctor = Doctor.builder()
                .userId(request.userId())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .specialization(request.specialization())
                .qualification(request.qualification())
                .experience(request.experience())
                .phone(request.phone())
                .email(request.email())
                .address(request.address())
                .status(DoctorStatus.PROFILE_INCOMPLETE)
                .build();

        return toResponse(doctorRepository.save(doctor));
    }

    @Transactional(readOnly = true)
    public DoctorResponse getDoctor(UUID id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Doctor not found"
                        )
                );

        return toResponse(doctor);
    }

    @Transactional(readOnly = true)
    public DoctorResponse getDoctorByUserId(UUID userId) {

        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Doctor profile not found"
                        )
                );

        return toResponse(doctor);
    }

    @Transactional(readOnly = true)
    public List<DoctorResponse> getAllDoctors() {

        return doctorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public DoctorResponse updateDoctor(
            UUID id,
            UpdateDoctorRequest request
    ) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Doctor not found"
                        )
                );

        doctor.setFirstName(request.firstName());
        doctor.setLastName(request.lastName());
        doctor.setSpecialization(request.specialization());
        doctor.setQualification(request.qualification());
        doctor.setExperience(request.experience());
        doctor.setPhone(request.phone());
        doctor.setEmail(request.email());
        doctor.setAddress(request.address());

        return toResponse(doctorRepository.save(doctor));
    }

    private DoctorResponse toResponse(Doctor doctor) {

        return new DoctorResponse(
                doctor.getId(),
                doctor.getUserId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialization(),
                doctor.getQualification(),
                doctor.getExperience(),
                doctor.getPhone(),
                doctor.getEmail(),
                doctor.getAddress(),
                doctor.getStatus(),
                doctor.getCreatedAt(),
                doctor.getUpdatedAt()
        );
    }
}