package com.hams.hospital_appointment_system.module.doctor.service.impl;

import com.hams.hospital_appointment_system.module.doctor.service.DoctorService;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorRequest;
import com.hams.hospital_appointment_system.module.doctor.dto.DoctorResponse;
import java.util.List;

public class DoctorServiceImpl implements DoctorService {

    @Override
    public DoctorResponse createDoctor(DoctorRequest doctorRequest) {
        // Implement the logic to create a doctor
        return null;
    }

    @Override
    public DoctorResponse getDoctorById(Long doctorId) {
        // Implement the logic to get a doctor by ID
        return null;
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        // Implement the logic to get all doctors
        return null;
    }

    @Override
    public DoctorResponse updateDoctor(Long doctorId, DoctorRequest doctorRequest) {
        // Implement the logic to update a doctor
        return null;
    }

    @Override
    public void deleteDoctor(Long doctorId) {
        // Implement the logic to delete a doctor
    }
}
