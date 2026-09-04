package com.hams.hospital_appointment_system.modules.patient.mapper;

import com.hams.hospital_appointment_system.module.patient.dto.PatientRequest;
import com.hams.hospital_appointment_system.module.patient.dto.PatientResponse;
import com.hams.hospital_appointment_system.common.enums.Gender;
import com.hams.hospital_appointment_system.module.patient.entity.Patient;
import com.hams.hospital_appointment_system.module.patient.mapper.PatientMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PatientMapperTest {

        @Test
        void toEntity_shouldMapRequestFieldsToEntity() {

                PatientRequest request = PatientRequest.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .gender(Gender.MALE)
                                .dateOfBirth(LocalDate.of(1990, 1, 1))
                                .email("john.doe@test.com")
                                .phone("9876543210")
                                .build();

                Patient patient = PatientMapper.toEntity(request);

                assertThat(patient.getId()).isNull();
                assertThat(patient.getFirstName()).isEqualTo("John");
                assertThat(patient.getLastName()).isEqualTo("Doe");
                assertThat(patient.getGender()).isEqualTo(Gender.MALE);
                assertThat(patient.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
                assertThat(patient.getEmail()).isEqualTo("john.doe@test.com");
                assertThat(patient.getPhone()).isEqualTo("9876543210");
        }

        @Test
        void toDto_shouldMapEntityFieldsToResponse() {

                Patient patient = Patient.builder()
                                .id(1L)
                                .firstName("Jane")
                                .lastName("Smith")
                                .gender(Gender.FEMALE)
                                .dateOfBirth(LocalDate.of(1992, 5, 10))
                                .email("jane.smith@test.com")
                                .phone("9876543211")
                                .build();

                PatientResponse response = PatientMapper.toDto(patient);

                assertThat(response.getId()).isEqualTo(1L);
                assertThat(response.getFirstName()).isEqualTo("Jane");
                assertThat(response.getLastName()).isEqualTo("Smith");
                assertThat(response.getGender()).isEqualTo(Gender.FEMALE);
                assertThat(response.getDateOfBirth()).isEqualTo(LocalDate.of(1992, 5, 10));
                assertThat(response.getEmail()).isEqualTo("jane.smith@test.com");
                assertThat(response.getPhone()).isEqualTo("9876543211");
        }

        @Test
        void updateEntity_shouldOverwriteExistingEntityFieldsFromRequest() {

                Patient patient = Patient.builder()
                                .id(1L)
                                .firstName("Old")
                                .lastName("Name")
                                .gender(Gender.MALE)
                                .dateOfBirth(LocalDate.of(1990, 1, 1))
                                .email("old@test.com")
                                .phone("9876543210")
                                .build();

                PatientRequest request = PatientRequest.builder()
                                .firstName("New")
                                .lastName("Name")
                                .gender(Gender.FEMALE)
                                .dateOfBirth(LocalDate.of(1995, 6, 15))
                                .email("new@test.com")
                                .phone("9876543211")
                                .build();

                Patient updatedPatient = PatientMapper.updateEntity(patient, request);

                assertThat(updatedPatient).isSameAs(patient);
                assertThat(updatedPatient.getId()).isEqualTo(1L);
                assertThat(updatedPatient.getFirstName()).isEqualTo("New");
                assertThat(updatedPatient.getLastName()).isEqualTo("Name");
                assertThat(updatedPatient.getGender()).isEqualTo(Gender.FEMALE);
                assertThat(updatedPatient.getDateOfBirth()).isEqualTo(LocalDate.of(1995, 6, 15));
                assertThat(updatedPatient.getEmail()).isEqualTo("new@test.com");
                assertThat(updatedPatient.getPhone()).isEqualTo("9876543211");
        }
}
