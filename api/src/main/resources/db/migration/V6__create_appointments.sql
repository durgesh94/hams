CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,

    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,

    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,

    reason VARCHAR(500),
    notes VARCHAR(1000),

    status VARCHAR(20) NOT NULL DEFAULT 'BOOKED',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_appointment_patient
        FOREIGN KEY (patient_id)
        REFERENCES patients(id),

    CONSTRAINT fk_appointment_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES doctors(id)
);

-- Indexes for the appointments table
CREATE INDEX idx_appointments_doctor_date
    ON appointments (doctor_id, appointment_date);

-- Index for the patient_id column
CREATE INDEX idx_appointments_patient
    ON appointments (patient_id);

-- Index for the status column
CREATE INDEX idx_appointments_status
    ON appointments (status);