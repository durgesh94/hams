package com.hams.hospital_appointment_system.modules.appointment.service;

import com.hams.hospital_appointment_system.common.enums.Gender;
import com.hams.hospital_appointment_system.common.exception.AppointmentSlotAlreadyBookedException;
import com.hams.hospital_appointment_system.common.exception.ResourceNotFoundException;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentRequest;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentResponse;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentFilter;
import com.hams.hospital_appointment_system.module.appointment.entity.AppointmentStatus;
import com.hams.hospital_appointment_system.module.appointment.entity.Appointment;
import com.hams.hospital_appointment_system.module.appointment.repository.AppointmentRepository;
import com.hams.hospital_appointment_system.module.appointment.service.impl.AppointmentPageableService;
import com.hams.hospital_appointment_system.module.appointment.service.impl.AppointmentServiceImpl;
import com.hams.hospital_appointment_system.module.doctor.entity.Doctor;
import com.hams.hospital_appointment_system.module.doctor.entity.DoctorStatus;
import com.hams.hospital_appointment_system.module.doctor.repository.DoctorRepository;
import com.hams.hospital_appointment_system.module.patient.entity.Patient;
import com.hams.hospital_appointment_system.module.patient.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

	@Mock
	private AppointmentPageableService appointmentPageableService;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private AppointmentRequest buildRequest() {
	return AppointmentRequest.builder()
		.patientId(2L)
		.doctorId(1L)
		.appointmentDate(LocalDate.of(2026, 10, 1))
		.appointmentTime(LocalTime.of(10, 30))
		.reason("Routine check-up")
		.notes("Bring previous reports")
		.build();
    }

    private Doctor buildDoctor(DoctorStatus status) {
	return Doctor.builder()
		.id(1L)
		.firstName("John")
		.lastName("Smith")
		.gender(Gender.MALE)
		.specialization("Cardiology")
		.qualification("MBBS, MD")
		.email("john.smith@test.com")
		.phone("9876543210")
		.experienceYears(10)
		.status(status)
		.build();
    }

    private Patient buildPatient() {
	return Patient.builder()
		.id(2L)
		.firstName("Jane")
		.lastName("Doe")
		.dateOfBirth(LocalDate.of(1990, 5, 20))
		.gender(Gender.FEMALE)
		.email("jane.doe@test.com")
		.phone("9876543211")
		.build();
    }

    private Appointment buildAppointment(Long id) {
	AppointmentRequest request = buildRequest();
	return Appointment.builder()
		.id(id)
		.doctor(buildDoctor(DoctorStatus.ACTIVE))
		.patient(buildPatient())
		.appointmentDate(request.getAppointmentDate())
		.appointmentTime(request.getAppointmentTime())
		.appointmentEndTime(request.getAppointmentTime().plusMinutes(30))
		.reason(request.getReason())
		.notes(request.getNotes())
		.status(AppointmentStatus.BOOKED)
		.build();
    }

    @Test
    void createAppointment_shouldSaveAndReturnAppointment_whenDoctorIsAvailable() {
	AppointmentRequest request = buildRequest();
	Doctor doctor = buildDoctor(DoctorStatus.ACTIVE);
	Patient patient = buildPatient();
	Appointment savedAppointment = Appointment.builder()
		.id(10L)
		.doctor(doctor)
		.patient(patient)
		.appointmentDate(request.getAppointmentDate())
		.appointmentTime(request.getAppointmentTime())
		.appointmentEndTime(request.getAppointmentTime().plusMinutes(30))
		.reason(request.getReason())
		.notes(request.getNotes())
		.status(AppointmentStatus.BOOKED)
		.build();

	when(doctorRepository.findById(request.getDoctorId())).thenReturn(Optional.of(doctor));
	when(patientRepository.findById(request.getPatientId())).thenReturn(Optional.of(patient));
	when(appointmentRepository
		.existsDoctorOverlappingAppointment(
			request.getDoctorId(), request.getAppointmentDate(), request.getAppointmentTime(),
		request.getAppointmentTime().plusMinutes(30),
			List.of(AppointmentStatus.BOOKED, AppointmentStatus.CONFIRMED)))
		.thenReturn(false);
	when(appointmentRepository.existsPatientOverlappingAppointment(
		request.getPatientId(), request.getAppointmentDate(), request.getAppointmentTime(),
		request.getAppointmentTime().plusMinutes(30),
		List.of(AppointmentStatus.BOOKED, AppointmentStatus.CONFIRMED)))
		.thenReturn(false);
	when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);

	AppointmentResponse response = appointmentService.createAppointment(request);

	assertThat(response.getId()).isEqualTo(10L);
	assertThat(response.getDoctorName()).isEqualTo("John Smith");
	assertThat(response.getPatientName()).isEqualTo("Jane Doe");
	assertThat(response.getStatus()).isEqualTo(AppointmentStatus.BOOKED);
	verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void createAppointment_shouldThrow_whenDoctorIsNotActive() {
	AppointmentRequest request = buildRequest();
	when(doctorRepository.findById(request.getDoctorId()))
		.thenReturn(Optional.of(buildDoctor(DoctorStatus.ON_LEAVE)));
	when(patientRepository.findById(request.getPatientId())).thenReturn(Optional.of(buildPatient()));

	assertThatThrownBy(() -> appointmentService.createAppointment(request))
		.isInstanceOf(AppointmentSlotAlreadyBookedException.class)
		.hasMessageContaining("Dr. John Smith is not available");

	verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void createAppointment_shouldThrow_whenAppointmentSlotIsAlreadyBooked() {
	AppointmentRequest request = buildRequest();
	when(doctorRepository.findById(request.getDoctorId()))
		.thenReturn(Optional.of(buildDoctor(DoctorStatus.ACTIVE)));
	when(patientRepository.findById(request.getPatientId())).thenReturn(Optional.of(buildPatient()));
	when(appointmentRepository
		.existsDoctorOverlappingAppointment(
			request.getDoctorId(), request.getAppointmentDate(), request.getAppointmentTime(),
		request.getAppointmentTime().plusMinutes(30),
			List.of(AppointmentStatus.BOOKED, AppointmentStatus.CONFIRMED)))
		.thenReturn(true);

	assertThatThrownBy(() -> appointmentService.createAppointment(request))
		.isInstanceOf(AppointmentSlotAlreadyBookedException.class)
		.hasMessageContaining("Dr. John Smith is already booked during the selected time");

	verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void createAppointment_shouldThrow_whenPatientAppointmentOverlaps() {
	AppointmentRequest request = buildRequest();
	when(doctorRepository.findById(request.getDoctorId()))
		.thenReturn(Optional.of(buildDoctor(DoctorStatus.ACTIVE)));
	when(patientRepository.findById(request.getPatientId())).thenReturn(Optional.of(buildPatient()));
	when(appointmentRepository.existsDoctorOverlappingAppointment(
		request.getDoctorId(), request.getAppointmentDate(), request.getAppointmentTime(),
		request.getAppointmentTime().plusMinutes(30),
		List.of(AppointmentStatus.BOOKED, AppointmentStatus.CONFIRMED)))
		.thenReturn(false);
	when(appointmentRepository.existsPatientOverlappingAppointment(
		request.getPatientId(), request.getAppointmentDate(), request.getAppointmentTime(),
		request.getAppointmentTime().plusMinutes(30),
		List.of(AppointmentStatus.BOOKED, AppointmentStatus.CONFIRMED)))
		.thenReturn(true);

	assertThatThrownBy(() -> appointmentService.createAppointment(request))
		.isInstanceOf(AppointmentSlotAlreadyBookedException.class)
		.hasMessageContaining("Patient already has an appointment during the selected time");

	verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void getAppointmentById_shouldThrow_whenAppointmentDoesNotExist() {
	when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

	assertThatThrownBy(() -> appointmentService.getAppointmentById(999L))
		.isInstanceOf(ResourceNotFoundException.class)
		.hasMessageContaining("Appointment not found with id 999");
    }

    @Test
    void getAppointmentById_shouldReturnAppointment_whenAppointmentExists() {
	when(appointmentRepository.findById(10L)).thenReturn(Optional.of(buildAppointment(10L)));

	AppointmentResponse response = appointmentService.getAppointmentById(10L);

	assertThat(response.getId()).isEqualTo(10L);
	assertThat(response.getDoctorName()).isEqualTo("John Smith");
	assertThat(response.getPatientName()).isEqualTo("Jane Doe");
    }

    @Test
    void createAppointment_shouldThrow_whenDoctorDoesNotExist() {
	AppointmentRequest request = buildRequest();
	when(doctorRepository.findById(request.getDoctorId())).thenReturn(Optional.empty());

	assertThatThrownBy(() -> appointmentService.createAppointment(request))
		.isInstanceOf(ResourceNotFoundException.class)
		.hasMessageContaining("Doctor not found with id 1");

	verify(patientRepository, never()).findById(any());
    }

    @Test
    void createAppointment_shouldThrow_whenPatientDoesNotExist() {
	AppointmentRequest request = buildRequest();
	when(doctorRepository.findById(request.getDoctorId()))
		.thenReturn(Optional.of(buildDoctor(DoctorStatus.ACTIVE)));
	when(patientRepository.findById(request.getPatientId())).thenReturn(Optional.empty());

	assertThatThrownBy(() -> appointmentService.createAppointment(request))
		.isInstanceOf(ResourceNotFoundException.class)
		.hasMessageContaining("Patient not found with id 2");

	verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void updateAppointment_shouldUpdateAndReturnAppointment_whenDependenciesExist() {
	Appointment existingAppointment = buildAppointment(10L);
	AppointmentRequest request = buildRequest();
	request.setReason("Updated reason");
	Doctor doctor = buildDoctor(DoctorStatus.ACTIVE);
	Patient patient = buildPatient();

	when(appointmentRepository.findById(10L)).thenReturn(Optional.of(existingAppointment));
	when(doctorRepository.findById(request.getDoctorId())).thenReturn(Optional.of(doctor));
	when(patientRepository.findById(request.getPatientId())).thenReturn(Optional.of(patient));
	when(appointmentRepository.save(existingAppointment)).thenReturn(existingAppointment);

	AppointmentResponse response = appointmentService.updateAppointment(10L, request);

	assertThat(response.getId()).isEqualTo(10L);
	assertThat(existingAppointment.getReason()).isEqualTo("Updated reason");
	assertThat(existingAppointment.getDoctor()).isEqualTo(doctor);
	assertThat(existingAppointment.getPatient()).isEqualTo(patient);
	verify(appointmentRepository).save(existingAppointment);
    }

    @Test
    void updateAppointment_shouldThrow_whenAppointmentDoesNotExist() {
	when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

	assertThatThrownBy(() -> appointmentService.updateAppointment(999L, buildRequest()))
		.isInstanceOf(ResourceNotFoundException.class)
		.hasMessageContaining("Appointment not found with id 999");

	verify(doctorRepository, never()).findById(any());
    }

    @Test
    void updateAppointment_shouldThrow_whenDoctorDoesNotExist() {
	when(appointmentRepository.findById(10L)).thenReturn(Optional.of(buildAppointment(10L)));
	when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

	assertThatThrownBy(() -> appointmentService.updateAppointment(10L, buildRequest()))
		.isInstanceOf(ResourceNotFoundException.class)
		.hasMessageContaining("Doctor not found with id 1");

	verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void updateAppointment_shouldThrow_whenPatientDoesNotExist() {
	when(appointmentRepository.findById(10L)).thenReturn(Optional.of(buildAppointment(10L)));
	when(doctorRepository.findById(1L)).thenReturn(Optional.of(buildDoctor(DoctorStatus.ACTIVE)));
	when(patientRepository.findById(2L)).thenReturn(Optional.empty());

	assertThatThrownBy(() -> appointmentService.updateAppointment(10L, buildRequest()))
		.isInstanceOf(ResourceNotFoundException.class)
		.hasMessageContaining("Patient not found with id 2");

	verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void updateAppointmentStatus_shouldUpdateAndReturnAppointment_whenAppointmentExists() {
	Appointment appointment = buildAppointment(10L);
	when(appointmentRepository.findById(10L)).thenReturn(Optional.of(appointment));
	when(appointmentRepository.save(appointment)).thenReturn(appointment);

	AppointmentResponse response = appointmentService.updateAppointmentStatus(10L, AppointmentStatus.CONFIRMED);

	assertThat(response.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
	assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
	verify(appointmentRepository).save(appointment);
    }

    @Test
    void updateAppointmentStatus_shouldThrow_whenAppointmentDoesNotExist() {
	when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

	assertThatThrownBy(() -> appointmentService.updateAppointmentStatus(999L, AppointmentStatus.CONFIRMED))
		.isInstanceOf(ResourceNotFoundException.class)
		.hasMessageContaining("Appointment not found with id 999");

	verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void getAppointments_shouldReturnMappedPage() {
	Appointment appointment = buildAppointment(10L);
	AppointmentFilter filter = new AppointmentFilter();
	Pageable pageable = PageRequest.of(0, 10);
	when(appointmentPageableService.create(pageable)).thenReturn(pageable);
	when(appointmentRepository.findAll(
		org.mockito.ArgumentMatchers.<Specification<Appointment>>any(), eq(pageable)))
		.thenReturn(new PageImpl<>(List.of(appointment), pageable, 1));

	var response = appointmentService.getAppointments(filter, pageable);

	assertThat(response.getTotalElements()).isEqualTo(1);
	assertThat(response.getContent()).hasSize(1);
	assertThat(response.getContent().get(0).getId()).isEqualTo(10L);
    }

    @Test
    void getAppointmentsList_shouldReturnMappedAppointments() {
	when(appointmentRepository.findAll()).thenReturn(List.of(buildAppointment(10L), buildAppointment(11L)));

	List<AppointmentResponse> responses = appointmentService.getAppointmentsList();

	assertThat(responses).hasSize(2);
	assertThat(responses.get(0).getId()).isEqualTo(10L);
	assertThat(responses.get(1).getId()).isEqualTo(11L);
    }

    @Test
    void deleteAppointment_shouldDeleteAppointment_whenAppointmentExists() {
	Appointment appointment = buildAppointment(10L);
	when(appointmentRepository.findById(10L)).thenReturn(Optional.of(appointment));

	assertThat(appointmentService.deleteAppointment(10L)).isNull();

	verify(appointmentRepository).delete(appointment);
    }

    @Test
    void deleteAppointment_shouldThrow_whenAppointmentDoesNotExist() {
	when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

	assertThatThrownBy(() -> appointmentService.deleteAppointment(999L))
		.isInstanceOf(ResourceNotFoundException.class)
		.hasMessageContaining("Appointment not found with id 999");

	verify(appointmentRepository, never()).delete(any(Appointment.class));
    }
}
