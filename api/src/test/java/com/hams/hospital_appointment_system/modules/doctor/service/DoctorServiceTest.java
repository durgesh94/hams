package com.hams.hospital_appointment_system.modules.doctor.service;

import com.hams.hospital_appointment_system.common.enums.Gender;
import com.hams.hospital_appointment_system.common.exception.DuplicateResourceException;
import com.hams.hospital_appointment_system.common.exception.ResourceNotFoundException;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorFilterRequest;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorRequest;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorResponse;
import com.hams.hospital_appointment_system.module.doctor.entity.Doctor;
import com.hams.hospital_appointment_system.module.doctor.entity.Status;
import com.hams.hospital_appointment_system.module.doctor.repository.DoctorRepository;
import com.hams.hospital_appointment_system.module.doctor.service.impl.DoctorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

        @Mock
        private DoctorRepository doctorRepository;

        @InjectMocks
        private DoctorServiceImpl doctorService;

        private DoctorRequest buildRequest() {
                return DoctorRequest.builder()
                                .firstName("John")
                                .lastName("Smith")
                                .gender(Gender.MALE)
                                .email("john.smith@test.com")
                                .specialization("Cardiology")
                                .qualification("MBBS, MD")
                                .experienceYears(10)
                                .phone("9876543210")
                                .status(Status.ACTIVE)
                                .build();
        }

        private Doctor buildDoctor(Long id) {
                return Doctor.builder()
                                .id(id)
                                .firstName("John")
                                .lastName("Smith")
                                .gender(Gender.MALE)
                                .email("john.smith@test.com")
                                .specialization("Cardiology")
                                .qualification("MBBS, MD")
                                .experienceYears(10)
                                .phone("9876543210")
                                .status(Status.ACTIVE)
                                .build();
        }

        @Test
        void createDoctor_shouldSaveAndReturnDoctor_whenEmailDoesNotExist() {

                DoctorRequest request = buildRequest();
                Doctor savedDoctor = buildDoctor(1L);

                when(doctorRepository.existsByEmail(request.getEmail())).thenReturn(false);
                when(doctorRepository.save(any(Doctor.class))).thenReturn(savedDoctor);

                DoctorResponse response = doctorService.createDoctor(request);

                assertThat(response.getId()).isEqualTo(1L);
                assertThat(response.getEmail()).isEqualTo("john.smith@test.com");
                verify(doctorRepository).save(any(Doctor.class));
        }

        @Test
        void createDoctor_shouldThrowDuplicateResourceException_whenEmailExists() {

                DoctorRequest request = buildRequest();

                when(doctorRepository.existsByEmail(request.getEmail())).thenReturn(true);

                assertThatThrownBy(() -> doctorService.createDoctor(request))
                                .isInstanceOf(DuplicateResourceException.class)
                                .hasMessageContaining(request.getEmail());

                verify(doctorRepository, never()).save(any(Doctor.class));
        }

        @Test
        void getDoctorById_shouldReturnDoctor_whenDoctorExists() {

                Doctor doctor = buildDoctor(1L);

                when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

                DoctorResponse response = doctorService.getDoctorById(1L);

                assertThat(response.getId()).isEqualTo(1L);
                assertThat(response.getEmail()).isEqualTo("john.smith@test.com");
        }

        @Test
        void getDoctorById_shouldThrowResourceNotFoundException_whenDoctorDoesNotExist() {

                when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

                assertThatThrownBy(() -> doctorService.getDoctorById(999L))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessageContaining("Doctor not found with id 999");
        }

        @Test
        void getAllDoctors_shouldReturnAllDoctors() {

                Doctor doctor1 = buildDoctor(1L);
                Doctor doctor2 = buildDoctor(2L);

                when(doctorRepository.findAll()).thenReturn(List.of(doctor1, doctor2));

                List<DoctorResponse> responses = doctorService.getAllDoctors();

                assertThat(responses).hasSize(2);
                assertThat(responses)
                                .extracting(DoctorResponse::getId)
                                .containsExactly(1L, 2L);
        }

        @Test
        void getAllDoctors_shouldReturnEmptyList_whenNoDoctorsExist() {

                when(doctorRepository.findAll()).thenReturn(List.of());

                List<DoctorResponse> responses = doctorService.getAllDoctors();

                assertThat(responses).isEmpty();
        }

        @Test
        void updateDoctor_shouldUpdateAndReturnDoctor_whenDoctorExists() {

                Doctor existingDoctor = buildDoctor(1L);

                DoctorRequest request = buildRequest();
                request.setFirstName("Updated");

                when(doctorRepository.findById(1L)).thenReturn(Optional.of(existingDoctor));
                when(doctorRepository.save(any(Doctor.class))).thenReturn(existingDoctor);

                DoctorResponse response = doctorService.updateDoctor(1L, request);

                assertThat(response.getFirstName()).isEqualTo("Updated");
                verify(doctorRepository).save(existingDoctor);
        }

        @Test
        void updateDoctor_shouldThrowResourceNotFoundException_whenDoctorDoesNotExist() {

                DoctorRequest request = buildRequest();

                when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

                assertThatThrownBy(() -> doctorService.updateDoctor(999L, request))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessageContaining("Doctor not found with id 999");

                verify(doctorRepository, never()).save(any(Doctor.class));
        }

        @Test
        void deleteDoctor_shouldDeleteDoctor_whenDoctorExists() {

                Doctor doctor = buildDoctor(1L);

                when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

                doctorService.deleteDoctor(1L);

                verify(doctorRepository).delete(doctor);
        }

        @Test
        void deleteDoctor_shouldThrowResourceNotFoundException_whenDoctorDoesNotExist() {

                when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

                assertThatThrownBy(() -> doctorService.deleteDoctor(999L))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessageContaining("Doctor not found with id 999");
        }

        @Test
        void getDoctorsByFilter_shouldReturnFilteredDoctors() {

                DoctorFilterRequest filterRequest = new DoctorFilterRequest();
                filterRequest.setGender(Gender.MALE);
                filterRequest.setSpecialization("Cardiology");
                filterRequest.setStatus(Status.ACTIVE);

                Doctor doctor = buildDoctor(1L);

                when(doctorRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class)))
                                .thenReturn(List.of(doctor));

                List<DoctorResponse> responses = doctorService.getDoctorsByFilter(filterRequest);

                assertThat(responses).hasSize(1);
                assertThat(responses.get(0).getId()).isEqualTo(1L);
        }

        @Test
        void getDoctorsByFilter_shouldReturnEmptyList_whenNoMatch() {

                DoctorFilterRequest filterRequest = new DoctorFilterRequest();

                when(doctorRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class)))
                                .thenReturn(List.of());

                List<DoctorResponse> responses = doctorService.getDoctorsByFilter(filterRequest);

                assertThat(responses).isEmpty();
        }
}
