CREATE TABLE patients (
    id UUID PRIMARY KEY,
    user_id UUID UNIQUE,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,

    gender VARCHAR(30),
    blood_group VARCHAR(30),

    phone_number VARCHAR(20),
    address VARCHAR(500),

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);