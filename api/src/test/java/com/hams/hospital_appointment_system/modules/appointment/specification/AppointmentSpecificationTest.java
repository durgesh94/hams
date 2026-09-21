package com.hams.hospital_appointment_system.modules.appointment.specification;

import com.hams.hospital_appointment_system.common.enums.Gender;
import com.hams.hospital_appointment_system.module.appointment.dto.AppointmentFilter;
import com.hams.hospital_appointment_system.module.appointment.entity.Appointment;
import com.hams.hospital_appointment_system.module.appointment.entity.AppointmentStatus;
import com.hams.hospital_appointment_system.module.appointment.repository.AppointmentRepository;
import com.hams.hospital_appointment_system.module.appointment.specification.AppointmentSpecification;
import com.hams.hospital_appointment_system.module.doctor.entity.Doctor;
import com.hams.hospital_appointment_system.module.doctor.entity.DoctorStatus;
import com.hams.hospital_appointment_system.module.doctor.repository.DoctorRepository;
import com.hams.hospital_appointment_system.module.patient.entity.Patient;
import com.hams.hospital_appointment_system.module.patient.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class AppointmentSpecificationTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    private Doctor buildDoctor(String email) {
	return doctorRepository.save(Doctor.builder()
		.firstName("John")
		.lastName("Smith")
		.gender(Gender.MALE)
		.specialization("Cardiology")
		.qualification("MBBS, MD")
		.email(email)
		.phone("9876543210")
		.experienceYears(10)
		.status(DoctorStatus.ACTIVE)
		.build());
    }

    private Patient buildPatient(String email) {
	return patientRepository.save(Patient.builder()
		.firstName("Jane")
		.lastName("Doe")
		.dateOfBirth(LocalDate.of(1990, 5, 20))
		.gender(Gender.FEMALE)
		.email(email)
		.phone("9876543211")
		.build());
    }

    private Appointment buildAppointment(
	    Doctor doctor,
	    Patient patient,
	    LocalDate date,
	    AppointmentStatus status) {
	return Appointment.builder()
		.doctor(doctor)
		.patient(patient)
		.appointmentDate(date)
		.appointmentTime(LocalTime.of(10, 30))
		.appointmentEndTime(LocalTime.of(11, 0))
		.reason("Routine check-up")
		.notes("Bring previous reports")
		.status(status)
		.createdAt(LocalDateTime.now())
		.updatedAt(LocalDateTime.now())
		.build();
    }

    @Test
    void filter_shouldReturnAllAppointments_whenNoFiltersProvided() {
	Doctor doctor = buildDoctor("doctor-all@test.com");
	Patient patient = buildPatient("patient-all@test.com");
	appointmentRepository.save(buildAppointment(doctor, patient, LocalDate.of(2026, 10, 1),
		AppointmentStatus.BOOKED));

	List<Appointment> results = appointmentRepository.findAll(
		AppointmentSpecification.filter(new AppointmentFilter()));

	assertThat(results).hasSize(1);
    }

    @Test
    void filter_shouldMatchDoctorId() {
	Doctor matchingDoctor = buildDoctor("doctor-match@test.com");
	Doctor otherDoctor = buildDoctor("doctor-other@test.com");
	Patient patient = buildPatient("patient-doctor@test.com");
	appointmentRepository.save(buildAppointment(matchingDoctor, patient, LocalDate.of(2026, 10, 1),
		AppointmentStatus.BOOKED));
	appointmentRepository.save(buildAppointment(otherDoctor, patient, LocalDate.of(2026, 10, 1),
		AppointmentStatus.BOOKED));

	AppointmentFilter filter = new AppointmentFilter();
	filter.setDoctorId(matchingDoctor.getId());

	List<Appointment> results = appointmentRepository.findAll(AppointmentSpecification.filter(filter));

	assertThat(results).extracting(appointment -> appointment.getDoctor().getId())
		.containsExactly(matchingDoctor.getId());
    }

    @Test
    void filter_shouldMatchPatientId() {
	Doctor doctor = buildDoctor("doctor-patient@test.com");
	Patient matchingPatient = buildPatient("patient-match@test.com");
	Patient otherPatient = buildPatient("patient-other@test.com");
	appointmentRepository.save(buildAppointment(doctor, matchingPatient, LocalDate.of(2026, 10, 1),
		AppointmentStatus.BOOKED));
	appointmentRepository.save(buildAppointment(doctor, otherPatient, LocalDate.of(2026, 10, 1),
		AppointmentStatus.BOOKED));

	AppointmentFilter filter = new AppointmentFilter();
	filter.setPatientId(matchingPatient.getId());

	List<Appointment> results = appointmentRepository.findAll(AppointmentSpecification.filter(filter));

	assertThat(results).extracting(appointment -> appointment.getPatient().getId())
		.containsExactly(matchingPatient.getId());
    }

    @Test
    void filter_shouldMatchDate() {
	Doctor doctor = buildDoctor("doctor-date@test.com");
	Patient patient = buildPatient("patient-date@test.com");
	LocalDate matchingDate = LocalDate.of(2026, 10, 1);
	appointmentRepository.save(buildAppointment(doctor, patient, matchingDate, AppointmentStatus.BOOKED));
	appointmentRepository.save(buildAppointment(doctor, patient, matchingDate.plusDays(1),
		AppointmentStatus.BOOKED));

	AppointmentFilter filter = new AppointmentFilter();
	filter.setDate(matchingDate);

	List<Appointment> results = appointmentRepository.findAll(AppointmentSpecification.filter(filter));

	assertThat(results).hasSize(1);
	assertThat(results.get(0).getAppointmentDate()).isEqualTo(matchingDate);
    }

    @Test
    void filter_shouldMatchStatus() {
	Doctor doctor = buildDoctor("doctor-status@test.com");
	Patient patient = buildPatient("patient-status@test.com");
	appointmentRepository.save(buildAppointment(doctor, patient, LocalDate.of(2026, 10, 1),
		AppointmentStatus.CONFIRMED));
	appointmentRepository.save(buildAppointment(doctor, patient, LocalDate.of(2026, 10, 2),
		AppointmentStatus.CANCELLED));

	AppointmentFilter filter = new AppointmentFilter();
	filter.setStatus(AppointmentStatus.CONFIRMED);

	List<Appointment> results = appointmentRepository.findAll(AppointmentSpecification.filter(filter));

	assertThat(results).hasSize(1);
	assertThat(results.get(0).getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
    }

    @Test
    void filter_shouldMatchAllProvidedFilters() {
	Doctor matchingDoctor = buildDoctor("doctor-combined@test.com");
	Doctor otherDoctor = buildDoctor("doctor-combined-other@test.com");
	Patient matchingPatient = buildPatient("patient-combined@test.com");
	Patient otherPatient = buildPatient("patient-combined-other@test.com");
	LocalDate matchingDate = LocalDate.of(2026, 10, 1);
	appointmentRepository.save(buildAppointment(matchingDoctor, matchingPatient, matchingDate,
		AppointmentStatus.CONFIRMED));
	appointmentRepository.save(buildAppointment(otherDoctor, otherPatient, matchingDate,
		AppointmentStatus.CONFIRMED));

	AppointmentFilter filter = new AppointmentFilter();
	filter.setDoctorId(matchingDoctor.getId());
	filter.setPatientId(matchingPatient.getId());
	filter.setDate(matchingDate);
	filter.setStatus(AppointmentStatus.CONFIRMED);

	List<Appointment> results = appointmentRepository.findAll(AppointmentSpecification.filter(filter));

	assertThat(results).hasSize(1);
	assertThat(results.get(0).getDoctor().getId()).isEqualTo(matchingDoctor.getId());
    }
}
