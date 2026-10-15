CREATE TABLE doctors (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,

    first_name VARCHAR(100),
    last_name VARCHAR(100),
    specialization VARCHAR(150),
    qualification VARCHAR(200),
    experience INTEGER,
    phone VARCHAR(20),
    email VARCHAR(150),
    address TEXT,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);