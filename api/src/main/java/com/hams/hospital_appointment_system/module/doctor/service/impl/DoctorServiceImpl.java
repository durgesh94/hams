package com.hams.hospital_appointment_system.module.doctor.service.impl;

import com.hams.hospital_appointment_system.module.doctor.service.DoctorService;
import com.hams.hospital_appointment_system.common.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;

import com.hams.hospital_appointment_system.module.doctor.dto.DoctorRequest;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorResponse;
import java.util.List;
import com.hams.hospital_appointment_system.module.doctor.entity.Doctor;
import com.hams.hospital_appointment_system.module.doctor.mapper.DoctorMapper;
import com.hams.hospital_appointment_system.module.doctor.repository.DoctorRepository;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public DoctorResponse createDoctor(DoctorRequest doctorRequest) {
        if (doctorRepository.existsByEmail(doctorRequest.getEmail())) {
            throw new DuplicateResourceException("Doctor with " + doctorRequest.getEmail() + " already exists");
        }

        Doctor doctor = DoctorMapper.toEntity(doctorRequest);
        doctor = doctorRepository.save(doctor);
        return DoctorMapper.toDto(doctor);
    }

    @Override
    public DoctorResponse getDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id " + doctorId));
        return DoctorMapper.toDto(doctor);
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(DoctorMapper::toDto)
                .toList();
    }

    @Override
    public DoctorResponse updateDoctor(Long doctorId, DoctorRequest doctorRequest) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id " + doctorId));
        doctor = DoctorMapper.updateEntity(doctor, doctorRequest);
        doctor = doctorRepository.save(doctor);
        return DoctorMapper.toDto(doctor);
    }

    @Override
    public void deleteDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id " + doctorId));
        doctorRepository.delete(doctor);
    }
}
