-- HealthShadow Database Initialization Script
-- This script runs automatically when the PostgreSQL container starts for the first time

-- Create users table
CREATE TABLE users
(
    id           SERIAL8 PRIMARY KEY,
    email        varchar(255) NOT NULL UNIQUE,
    password_hash varchar(255) NOT NULL,
    user_type    varchar(20)  NOT NULL CHECK (user_type IN ('student', 'professional', 'admin')),
    created_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    is_active    BOOLEAN      DEFAULT TRUE
);

-- Create student table
CREATE TABLE student
(
    id         SERIAL8 PRIMARY KEY,
    user_id    BIGINT      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    first_name varchar(100) NOT NULL,
    last_name  varchar(100) NOT NULL,
    phone      VARCHAR(15) NOT NULL,
    student_id varchar(50) UNIQUE,
    major      varchar(100),
    year_level INTEGER,
    gpa        DECIMAL(3,2),
    created_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

-- Create professional table
CREATE TABLE professional
(
    id                      SERIAL8 PRIMARY KEY,
    user_id                 BIGINT      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    first_name              varchar(100) NOT NULL,
    last_name               varchar(100) NOT NULL,
    phone                   VARCHAR(15) NOT NULL,
    professional_type       varchar(50) NOT NULL, -- MD, DO, PA, NP, etc.
    license_number          varchar(50),
    license_state           varchar(2), -- State where licensed
    specialization          varchar(100), -- FAMILY_MEDICINE, CARDIOLOGY, etc.
    years_experience        INTEGER,
    practice_type           varchar(50), -- HOSPITAL, PRIVATE_PRACTICE, CLINIC, etc.
    practice_name           varchar(200), -- Name of practice/hospital
    practice_address        TEXT,
    practice_city           varchar(100),
    practice_state          varchar(2),
    practice_zip            varchar(10),
    title_position          varchar(100), -- Attending Physician, Chief Resident, etc.
    bio                     TEXT,
    photo_url               TEXT,
    availability            TEXT, -- JSON array of {day, timeRanges} objects
    availability_notes      TEXT, -- Additional notes about availability
    verified                BOOLEAN     DEFAULT FALSE,
    created_at              TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

-- Create contact_submission table
CREATE TABLE IF NOT EXISTS contact_submission
(
    id           SERIAL8 PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    user_type    VARCHAR(20)  NOT NULL CHECK (user_type IN ('professional', 'student')),
    message      TEXT         NOT NULL,
    created_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- Create chat_message table
CREATE TABLE IF NOT EXISTS chat_message
(
    id           SERIAL8 PRIMARY KEY,
    user_id      BIGINT       REFERENCES users(id) ON DELETE CASCADE,
    role         VARCHAR(20)  NOT NULL CHECK (role IN ('user', 'assistant')),
    content      TEXT         NOT NULL,
    created_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_user_type ON users(user_type);
CREATE INDEX idx_student_user_id ON student(user_id);
CREATE INDEX idx_professional_user_id ON professional(user_id);
CREATE INDEX idx_professional_specialization ON professional(specialization);
CREATE INDEX idx_professional_location ON professional(practice_city, practice_state);
CREATE INDEX idx_contact_submission_created_at ON contact_submission(created_at);
CREATE INDEX idx_chat_message_user_id ON chat_message(user_id);
CREATE INDEX idx_chat_message_created_at ON chat_message(created_at DESC);

-- Insert sample data with proper foreign key relationships
-- Insert users and capture their IDs for proper linking
-- COMMENTED OUT FOR PRODUCTION - Only use for local development

-- -- Insert student user and get the ID
-- WITH student_user AS (
--     INSERT INTO users (email, password_hash, user_type)
--     VALUES ('student@example.com', 'TODO_IMPLEMENT_PASSWORD_HASHING', 'student')
--     RETURNING id
-- )
-- INSERT INTO student (user_id, first_name, last_name, phone, student_id, major, year_level, gpa)
-- SELECT id, 'John', 'Doe', '555-0123', 'STU001', 'Computer Science', 3, 3.75
-- FROM student_user;

-- -- Insert professional user and get the ID
-- WITH professional_user AS (
--     INSERT INTO users (email, password_hash, user_type)
--     VALUES ('professional@example.com', 'TODO_IMPLEMENT_PASSWORD_HASHING', 'professional')
--     RETURNING id
-- )
-- INSERT INTO professional (user_id, first_name, last_name, phone, professional_type, license_number, license_state, specialization, years_experience, practice_type, practice_name, practice_city, practice_state, title_position, verified)
-- SELECT id, 'Dr. Jane', 'Smith', '555-0456', 'MD', 'MD123456', 'CA', 'INTERNAL_MEDICINE', 8, 'HOSPITAL', 'City Hospital', 'San Francisco', 'CA', 'Attending Physician', true
-- FROM professional_user;

-- -- Insert admin user (no additional profile needed)
-- INSERT INTO users (email, password_hash, user_type)
-- VALUES ('admin@example.com', 'TODO_IMPLEMENT_PASSWORD_HASHING', 'admin');