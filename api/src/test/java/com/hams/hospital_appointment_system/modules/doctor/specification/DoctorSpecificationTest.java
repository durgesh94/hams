package com.hams.hospital_appointment_system.modules.doctor.specification;

import com.hams.hospital_appointment_system.common.enums.Gender;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorFilterRequest;
import com.hams.hospital_appointment_system.module.doctor.entity.Doctor;
import com.hams.hospital_appointment_system.module.doctor.entity.DoctorStatus;
import com.hams.hospital_appointment_system.module.doctor.repository.DoctorRepository;
import com.hams.hospital_appointment_system.module.doctor.specification.DoctorSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class DoctorSpecificationTest {

        @Autowired
        private DoctorRepository doctorRepository;

        private Doctor buildDoctor(String email, Gender gender, String specialization, DoctorStatus status) {
                return Doctor.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .gender(gender)
                                .email(email)
                                .specialization(specialization)
                                .qualification("MBBS")
                                .experienceYears(5)
                                .phone("9876543210")
                                .status(status)
                                .build();
        }

        @Test
        void byFilter_shouldReturnDoctors_matchingGender() {

                doctorRepository.save(buildDoctor("male1@test.com", Gender.MALE, "Cardiology", DoctorStatus.ACTIVE));
                doctorRepository.save(buildDoctor("female1@test.com", Gender.FEMALE, "Cardiology", DoctorStatus.ACTIVE));

                DoctorFilterRequest filter = new DoctorFilterRequest();
                filter.setGender(Gender.MALE);

                List<Doctor> results = doctorRepository.findAll(DoctorSpecification.byFilter(filter));

                assertThat(results).hasSize(1);
                assertThat(results.get(0).getEmail()).isEqualTo("male1@test.com");
        }

        @Test
        void byFilter_shouldReturnDoctors_matchingSpecializationCaseInsensitive() {

                doctorRepository.save(buildDoctor("cardio@test.com", Gender.MALE, "Cardiology", DoctorStatus.ACTIVE));
                doctorRepository.save(buildDoctor("neuro@test.com", Gender.MALE, "Neurology", DoctorStatus.ACTIVE));

                DoctorFilterRequest filter = new DoctorFilterRequest();
                filter.setSpecialization("CARDIOLOGY");

                List<Doctor> results = doctorRepository.findAll(DoctorSpecification.byFilter(filter));

                assertThat(results).hasSize(1);
                assertThat(results.get(0).getEmail()).isEqualTo("cardio@test.com");
        }

        @Test
        void byFilter_shouldReturnDoctors_matchingStatus() {

                doctorRepository.save(buildDoctor("active@test.com", Gender.MALE, "Cardiology", DoctorStatus.ACTIVE));
                doctorRepository.save(buildDoctor("inactive@test.com", Gender.MALE, "Cardiology", DoctorStatus.INACTIVE));

                DoctorFilterRequest filter = new DoctorFilterRequest();
                filter.setStatus(DoctorStatus.INACTIVE);

                List<Doctor> results = doctorRepository.findAll(DoctorSpecification.byFilter(filter));

                assertThat(results).hasSize(1);
                assertThat(results.get(0).getEmail()).isEqualTo("inactive@test.com");
        }

        @Test
        void byFilter_shouldReturnDoctors_matchingAllFiltersCombined() {

                doctorRepository.save(buildDoctor("match@test.com", Gender.FEMALE, "Neurology", DoctorStatus.ACTIVE));
                doctorRepository.save(buildDoctor("nomatch1@test.com", Gender.MALE, "Neurology", DoctorStatus.ACTIVE));
                doctorRepository.save(buildDoctor("nomatch2@test.com", Gender.FEMALE, "Cardiology", DoctorStatus.ACTIVE));
                doctorRepository.save(buildDoctor("nomatch3@test.com", Gender.FEMALE, "Neurology", DoctorStatus.INACTIVE));

                DoctorFilterRequest filter = new DoctorFilterRequest();
                filter.setGender(Gender.FEMALE);
                filter.setSpecialization("Neurology");
                filter.setStatus(DoctorStatus.ACTIVE);

                List<Doctor> results = doctorRepository.findAll(DoctorSpecification.byFilter(filter));

                assertThat(results).hasSize(1);
                assertThat(results.get(0).getEmail()).isEqualTo("match@test.com");
        }

        @Test
        void byFilter_shouldReturnAllDoctors_whenNoFiltersProvided() {

                doctorRepository.save(buildDoctor("doc1@test.com", Gender.MALE, "Cardiology", DoctorStatus.ACTIVE));
                doctorRepository.save(buildDoctor("doc2@test.com", Gender.FEMALE, "Neurology", DoctorStatus.INACTIVE));

                DoctorFilterRequest filter = new DoctorFilterRequest();

                List<Doctor> results = doctorRepository.findAll(DoctorSpecification.byFilter(filter));

                assertThat(results).hasSize(2);
        }
}
