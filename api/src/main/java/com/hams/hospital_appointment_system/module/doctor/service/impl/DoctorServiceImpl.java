package com.hams.hospital_appointment_system.module.doctor.service.impl;

import com.hams.hospital_appointment_system.module.doctor.service.DoctorService;
import com.hams.hospital_appointment_system.common.exception.DuplicateResourceException;
import com.hams.hospital_appointment_system.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

import com.hams.hospital_appointment_system.module.doctor.dto.DoctorRequest;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorResponse;
import com.hams.hospital_appointment_system.module.appointment.repository.AppointmentRepository;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorFilterRequest;
import com.hams.hospital_appointment_system.module.doctor.specification.DoctorSpecification;
import java.util.List;
import com.hams.hospital_appointment_system.module.doctor.entity.Doctor;
import com.hams.hospital_appointment_system.module.doctor.mapper.DoctorMapper;
import com.hams.hospital_appointment_system.module.doctor.repository.DoctorRepository;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

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
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + doctorId));
        long appointmentCount = appointmentRepository.countByDoctorId(doctor.getId());
        DoctorResponse doctorResponse = DoctorMapper.toDto(doctor);
        doctorResponse.setAppointmentCount(appointmentCount);
        return doctorResponse;
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(doctor -> {
                    DoctorResponse doctorResponse = DoctorMapper.toDto(doctor);
                    long appointmentCount = appointmentRepository.countByDoctorId(doctor.getId());
                    doctorResponse.setAppointmentCount(appointmentCount);
                    return doctorResponse;
                })
                .toList();
    }

    @Override
    public DoctorResponse updateDoctor(Long doctorId, DoctorRequest doctorRequest) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + doctorId));
        doctor = DoctorMapper.updateEntity(doctor, doctorRequest);
        doctor = doctorRepository.save(doctor);
        return DoctorMapper.toDto(doctor);
    }

    @Override
    public void deleteDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + doctorId));
        doctorRepository.delete(doctor);
    }

    @Override
    public List<DoctorResponse> getDoctorsByFilter(DoctorFilterRequest filterRequest) {
        List<Doctor> doctors = doctorRepository.findAll(DoctorSpecification.byFilter(filterRequest));
        return doctors.stream()
                .map(DoctorMapper::toDto)
                .toList();
    }
}
