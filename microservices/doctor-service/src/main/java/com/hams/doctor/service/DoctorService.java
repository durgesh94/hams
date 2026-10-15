package com.hams.doctor.service;

import com.hams.doctor.dto.CreateDoctorRequest;
import com.hams.doctor.dto.CreateProfileRequest;
import com.hams.doctor.dto.DoctorResponse;
import com.hams.doctor.dto.DoctorReviewRequest;
import com.hams.doctor.dto.UpdateDoctorRequest;
import com.hams.doctor.entity.Doctor;
import com.hams.doctor.entity.DoctorStatus;
import com.hams.doctor.exception.DuplicateResourceException;
import com.hams.doctor.exception.InvalidDoctorReviewStateException;
import com.hams.doctor.exception.ResourceNotFoundException;
import com.hams.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import com.hams.doctor.client.AuthClient;
import com.hams.doctor.client.dto.AuthUserResponse;
import com.hams.doctor.client.dto.CreateUserRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {

        private final DoctorRepository doctorRepository;
        private final AuthClient authClient;

        @Transactional
        public DoctorResponse createDoctor(CreateDoctorRequest request) {

                // 1. Create the authentication account.
                CreateUserRequest userRequest = new CreateUserRequest(
                                request.username(),
                                request.password(),
                                "DOCTOR");

                AuthUserResponse authUser = authClient.createUser(userRequest);

                // 2. Use the user ID returned by Auth Service.
                UUID userId = authUser.id();

                Doctor doctor = Doctor.builder()
                                .userId(userId)
                                .firstName(request.firstName())
                                .lastName(request.lastName())
                                .specialization(request.specialization())
                                .qualification(request.qualification())
                                .experience(request.experience())
                                .phone(request.phone())
                                .email(request.email())
                                .address(request.address())
                                .status(DoctorStatus.ACTIVE)
                                .build();
                // 3. Save the doctor profile in Doctor DB.
                Doctor savedDoctor = doctorRepository.save(doctor);

                return toResponse(savedDoctor);
        }

        @Transactional
        public DoctorResponse createProfile(CreateProfileRequest request, UUID userId) {

                // 1. Check the doctor profile does not already exist for this user.
                if (doctorRepository.findByUserId(userId).isPresent()) {
                        throw new DuplicateResourceException("Doctor profile already exists for this user");
                }

                // 2. Create the doctor profile with PENDING_APPROVAL status.

                Doctor doctor = Doctor.builder()
                                .userId(userId)
                                .firstName(request.firstName())
                                .lastName(request.lastName())
                                .specialization(request.specialization())
                                .qualification(request.qualification())
                                .experience(request.experience())
                                .phone(request.phone())
                                .email(request.email())
                                .address(request.address())
                                .status(DoctorStatus.PENDING_APPROVAL)
                                .build();

                Doctor savedDoctor = doctorRepository.save(doctor);
                return toResponse(savedDoctor);
        }

        @Transactional(readOnly = true)
        public DoctorResponse getDoctor(UUID id) {

                Doctor doctor = doctorRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Doctor not found"));

                return toResponse(doctor);
        }

        @Transactional(readOnly = true)
        public DoctorResponse getDoctorByUserId(UUID userId) {

                Doctor doctor = doctorRepository.findByUserId(userId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Doctor profile not found"));

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
                        UpdateDoctorRequest request) {

                Doctor doctor = doctorRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Doctor not found"));

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

        @Transactional
        public DoctorResponse reviewDoctor(
                        UUID doctorId,
                        DoctorReviewRequest request) {

                Doctor doctor = doctorRepository.findById(doctorId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Doctor profile not found: " + doctorId));

                if (doctor.getStatus() != DoctorStatus.PENDING_APPROVAL) {
                        throw new InvalidDoctorReviewStateException(
                                        "Only pending doctor profiles can be reviewed");
                }

                DoctorStatus newStatus = switch (request.decision()) {
                        case APPROVED -> DoctorStatus.ACTIVE;
                        case REJECTED -> DoctorStatus.REJECTED;
                };

                doctor.setStatus(newStatus);
                // doctor.setReviewRemarks(request.remarks());

                Doctor savedDoctor = doctorRepository.save(doctor);

                return toResponse(savedDoctor);
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
                                doctor.getUpdatedAt());
        }
}