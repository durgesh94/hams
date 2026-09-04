package com.hams.hospital_appointment_system.modules.patient.service.impl;

import com.hams.hospital_appointment_system.common.exception.DuplicateResourceException;
import com.hams.hospital_appointment_system.common.exception.ResourceNotFoundException;
import com.hams.hospital_appointment_system.module.patient.dto.PatientRequest;
import com.hams.hospital_appointment_system.module.patient.dto.PatientResponse;
import com.hams.hospital_appointment_system.module.patient.entity.Patient;
import com.hams.hospital_appointment_system.module.patient.mapper.PatientMapper;
import com.hams.hospital_appointment_system.module.patient.repository.PatientRepository;
import com.hams.hospital_appointment_system.module.patient.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    void createPatient_shouldCreatePatientSuccessfully() {
        PatientRequest request = mock(PatientRequest.class);
        Patient patient = mock(Patient.class);
        Patient savedPatient = mock(Patient.class);
        PatientResponse response = mock(PatientResponse.class);

        when(request.getEmail()).thenReturn("patient@test.com");
        when(patientRepository.existsByEmail("patient@test.com")).thenReturn(false);
        when(patientRepository.save(patient)).thenReturn(savedPatient);

        try (MockedStatic<PatientMapper> mockedMapper = mockStatic(PatientMapper.class)) {

            mockedMapper.when(() -> PatientMapper.toEntity(request))
                    .thenReturn(patient);

            mockedMapper.when(() -> PatientMapper.toDto(savedPatient))
                    .thenReturn(response);

            PatientResponse result = patientService.createPatient(request);

            assertThat(result).isSameAs(response);

            verify(patientRepository).existsByEmail("patient@test.com");
            verify(patientRepository).save(patient);

            mockedMapper.verify(() -> PatientMapper.toEntity(request));
            mockedMapper.verify(() -> PatientMapper.toDto(savedPatient));
        }
    }

    @Test
    void createPatient_shouldThrowDuplicateResourceException_whenEmailAlreadyExists() {
        PatientRequest request = mock(PatientRequest.class);

        when(request.getEmail()).thenReturn("patient@test.com");
        when(patientRepository.existsByEmail("patient@test.com")).thenReturn(true);

        assertThatThrownBy(() -> patientService.createPatient(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Patient with email patient@test.com already exists");

        verify(patientRepository).existsByEmail("patient@test.com");
        verify(patientRepository, never()).save(any());
    }

    @Test
    void getPatientById_shouldReturnPatientSuccessfully() {
        Long patientId = 1L;

        Patient patient = mock(Patient.class);
        PatientResponse response = mock(PatientResponse.class);

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(patient));

        try (MockedStatic<PatientMapper> mockedMapper = mockStatic(PatientMapper.class)) {

            mockedMapper.when(() -> PatientMapper.toDto(patient))
                    .thenReturn(response);

            PatientResponse result = patientService.getPatientById(patientId);

            assertThat(result).isSameAs(response);

            verify(patientRepository).findById(patientId);
            mockedMapper.verify(() -> PatientMapper.toDto(patient));
        }
    }

    @Test
    void getPatientById_shouldThrowResourceNotFoundException_whenPatientDoesNotExist() {
        Long patientId = 999L;

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.getPatientById(patientId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Patient with id '999' not found");

        verify(patientRepository).findById(patientId);
    }

    @Test
    void getAllPatients_shouldReturnAllPatientsSuccessfully() {
        Patient patient1 = mock(Patient.class);
        Patient patient2 = mock(Patient.class);

        PatientResponse response1 = mock(PatientResponse.class);
        PatientResponse response2 = mock(PatientResponse.class);

        when(patientRepository.findAll())
                .thenReturn(List.of(patient1, patient2));

        try (MockedStatic<PatientMapper> mockedMapper = mockStatic(PatientMapper.class)) {

            mockedMapper.when(() -> PatientMapper.toDto(patient1))
                    .thenReturn(response1);

            mockedMapper.when(() -> PatientMapper.toDto(patient2))
                    .thenReturn(response2);

            List<PatientResponse> result = patientService.getAllPatients();

            assertThat(result)
                    .containsExactly(response1, response2);

            verify(patientRepository).findAll();

            mockedMapper.verify(() -> PatientMapper.toDto(patient1));
            mockedMapper.verify(() -> PatientMapper.toDto(patient2));
        }
    }

    @Test
    void getAllPatients_shouldReturnEmptyList_whenNoPatientsExist() {
        when(patientRepository.findAll())
                .thenReturn(List.of());

        List<PatientResponse> result = patientService.getAllPatients();

        assertThat(result).isEmpty();

        verify(patientRepository).findAll();
    }

    @Test
    void updatePatientById_shouldUpdatePatientSuccessfully() {
        Long patientId = 1L;

        PatientRequest request = mock(PatientRequest.class);
        Patient existingPatient = mock(Patient.class);
        Patient updatedPatient = mock(Patient.class);
        Patient savedPatient = mock(Patient.class);
        PatientResponse response = mock(PatientResponse.class);

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(existingPatient));

        when(patientRepository.save(updatedPatient))
                .thenReturn(savedPatient);

        try (MockedStatic<PatientMapper> mockedMapper = mockStatic(PatientMapper.class)) {

            mockedMapper.when(() -> PatientMapper.updateEntity(existingPatient, request))
                    .thenReturn(updatedPatient);

            mockedMapper.when(() -> PatientMapper.toDto(savedPatient))
                    .thenReturn(response);

            PatientResponse result =
                    patientService.updatePatientById(patientId, request);

            assertThat(result).isSameAs(response);

            verify(patientRepository).findById(patientId);
            verify(patientRepository).save(updatedPatient);

            mockedMapper.verify(
                    () -> PatientMapper.updateEntity(existingPatient, request)
            );

            mockedMapper.verify(
                    () -> PatientMapper.toDto(savedPatient)
            );
        }
    }

    @Test
    void updatePatientById_shouldThrowResourceNotFoundException_whenPatientDoesNotExist() {
        Long patientId = 999L;

        PatientRequest request = mock(PatientRequest.class);

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> patientService.updatePatientById(patientId, request)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Patient with id '999' not found");

        verify(patientRepository).findById(patientId);
        verify(patientRepository, never()).save(any());
    }

    @Test
    void deletePatientById_shouldDeletePatientSuccessfully() {
        Long patientId = 1L;

        Patient patient = mock(Patient.class);
        PatientResponse response = mock(PatientResponse.class);

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(patient));

        try (MockedStatic<PatientMapper> mockedMapper = mockStatic(PatientMapper.class)) {

            mockedMapper.when(() -> PatientMapper.toDto(patient))
                    .thenReturn(response);

            PatientResponse result =
                    patientService.deletePatientById(patientId);

            assertThat(result).isSameAs(response);

            verify(patientRepository).findById(patientId);
            verify(patientRepository).delete(patient);

            mockedMapper.verify(() -> PatientMapper.toDto(patient));
        }
    }

    @Test
    void deletePatientById_shouldThrowResourceNotFoundException_whenPatientDoesNotExist() {
        Long patientId = 999L;

        when(patientRepository.findById(patientId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> patientService.deletePatientById(patientId)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Patient with id '999' not found");

        verify(patientRepository).findById(patientId);
        verify(patientRepository, never()).delete(any());
    }
}