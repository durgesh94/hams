package com.hams.hospital_appointment_system.module.doctor.service;

import java.util.List;

import com.hams.hospital_appointment_system.module.doctor.dto.DoctorFilterRequest;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorRequest;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorResponse;

public interface DoctorService {

    DoctorResponse createDoctor(DoctorRequest doctorRequest);

    DoctorResponse getDoctorById(Long doctorId);

    List<DoctorResponse> getAllDoctors();

    DoctorResponse updateDoctor(Long doctorId, DoctorRequest doctorRequest);

    void deleteDoctor(Long doctorId);

    List<DoctorResponse> getDoctorsByFilter(DoctorFilterRequest filterRequest);
}
