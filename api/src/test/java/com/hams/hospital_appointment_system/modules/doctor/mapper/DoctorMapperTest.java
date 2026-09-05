package com.hams.hospital_appointment_system.modules.doctor.mapper;

import com.hams.hospital_appointment_system.common.enums.Gender;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorRequest;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorResponse;
import com.hams.hospital_appointment_system.module.doctor.entity.Doctor;
import com.hams.hospital_appointment_system.module.doctor.entity.Status;
import com.hams.hospital_appointment_system.module.doctor.mapper.DoctorMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DoctorMapperTest {

        @Test
        void toEntity_shouldMapRequestFieldsToEntity() {

                DoctorRequest request = DoctorRequest.builder()
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

                Doctor doctor = DoctorMapper.toEntity(request);

                assertThat(doctor.getId()).isNull();
                assertThat(doctor.getFirstName()).isEqualTo("John");
                assertThat(doctor.getLastName()).isEqualTo("Smith");
                assertThat(doctor.getGender()).isEqualTo(Gender.MALE);
                assertThat(doctor.getEmail()).isEqualTo("john.smith@test.com");
                assertThat(doctor.getSpecialization()).isEqualTo("Cardiology");
                assertThat(doctor.getQualification()).isEqualTo("MBBS, MD");
                assertThat(doctor.getExperienceYears()).isEqualTo(10);
                assertThat(doctor.getPhone()).isEqualTo("9876543210");
                assertThat(doctor.getStatus()).isEqualTo(Status.ACTIVE);
        }

        @Test
        void toDto_shouldMapEntityFieldsToResponse() {

                Doctor doctor = Doctor.builder()
                                .id(1L)
                                .firstName("Jane")
                                .lastName("Doe")
                                .gender(Gender.FEMALE)
                                .email("jane.doe@test.com")
                                .specialization("Neurology")
                                .qualification("MBBS, DM")
                                .experienceYears(8)
                                .phone("9876543211")
                                .status(Status.INACTIVE)
                                .build();

                DoctorResponse response = DoctorMapper.toDto(doctor);

                assertThat(response.getId()).isEqualTo(1L);
                assertThat(response.getFirstName()).isEqualTo("Jane");
                assertThat(response.getLastName()).isEqualTo("Doe");
                assertThat(response.getGender()).isEqualTo("FEMALE");
                assertThat(response.getEmail()).isEqualTo("jane.doe@test.com");
                assertThat(response.getSpecialization()).isEqualTo("Neurology");
                assertThat(response.getQualification()).isEqualTo("MBBS, DM");
                assertThat(response.getExperienceYears()).isEqualTo(8);
                assertThat(response.getPhone()).isEqualTo("9876543211");
                assertThat(response.getStatus()).isEqualTo("INACTIVE");
        }

        @Test
        void updateEntity_shouldOverwriteExistingEntityFieldsFromRequest() {

                Doctor doctor = Doctor.builder()
                                .id(1L)
                                .firstName("Old")
                                .lastName("Name")
                                .gender(Gender.MALE)
                                .email("old@test.com")
                                .specialization("Cardiology")
                                .qualification("MBBS")
                                .experienceYears(5)
                                .phone("9876543210")
                                .status(Status.ACTIVE)
                                .build();

                DoctorRequest request = DoctorRequest.builder()
                                .firstName("New")
                                .lastName("Name")
                                .gender(Gender.FEMALE)
                                .email("new@test.com")
                                .specialization("Neurology")
                                .qualification("MBBS, MD")
                                .experienceYears(12)
                                .phone("9876543211")
                                .status(Status.INACTIVE)
                                .build();

                Doctor updatedDoctor = DoctorMapper.updateEntity(doctor, request);

                assertThat(updatedDoctor).isSameAs(doctor);
                assertThat(updatedDoctor.getId()).isEqualTo(1L);
                assertThat(updatedDoctor.getFirstName()).isEqualTo("New");
                assertThat(updatedDoctor.getLastName()).isEqualTo("Name");
                assertThat(updatedDoctor.getGender()).isEqualTo(Gender.FEMALE);
                assertThat(updatedDoctor.getEmail()).isEqualTo("new@test.com");
                assertThat(updatedDoctor.getSpecialization()).isEqualTo("Neurology");
                assertThat(updatedDoctor.getQualification()).isEqualTo("MBBS, MD");
                assertThat(updatedDoctor.getExperienceYears()).isEqualTo(12);
                assertThat(updatedDoctor.getPhone()).isEqualTo("9876543211");
                assertThat(updatedDoctor.getStatus()).isEqualTo(Status.INACTIVE);
        }
}
