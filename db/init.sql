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
    license_number          varchar(50) UNIQUE,
    specialization          varchar(100), -- FAMILY_MEDICINE, CARDIOLOGY, etc.
    years_experience        INTEGER,
    organization            varchar(200),
    practice_type           varchar(50), -- HOSPITAL, PRIVATE_PRACTICE, CLINIC, etc.
    practice_city           varchar(100),
    practice_state          varchar(2),
    practice_address        TEXT,
    specialties             TEXT, -- JSON array of additional specialties
    student_requirements    TEXT, -- requirements description
    available_days          TEXT, -- JSON Array ["Monday", "Tuesday"]
    available_times         TEXT,
    title                   varchar(100),
    bio                     TEXT,
    photo_url               TEXT,
    verified                BOOLEAN     DEFAULT FALSE,
    created_at              TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_user_type ON users(user_type);
CREATE INDEX idx_student_user_id ON student(user_id);
CREATE INDEX idx_professional_user_id ON professional(user_id);
CREATE INDEX idx_professional_specialization ON professional(specialization);

-- Insert sample data with proper foreign key relationships
-- Insert users and capture their IDs for proper linking

-- Insert student user and get the ID
WITH student_user AS (
    INSERT INTO users (email, password_hash, user_type) 
    VALUES ('student@example.com', 'TODO_IMPLEMENT_PASSWORD_HASHING', 'student')
    RETURNING id
)
INSERT INTO student (user_id, first_name, last_name, phone, student_id, major, year_level, gpa)
SELECT id, 'John', 'Doe', '555-0123', 'STU001', 'Computer Science', 3, 3.75
FROM student_user;

-- Insert professional user and get the ID
WITH professional_user AS (
    INSERT INTO users (email, password_hash, user_type) 
    VALUES ('professional@example.com', 'TODO_IMPLEMENT_PASSWORD_HASHING', 'professional')
    RETURNING id
)
INSERT INTO professional (user_id, first_name, last_name, phone, professional_type, license_number, specialization, years_experience, organization, practice_type, title, verified)
SELECT id, 'Dr. Jane', 'Smith', '555-0456', 'MD', 'MD123456', 'INTERNAL_MEDICINE', 8, 'City Hospital', 'HOSPITAL', 'Attending Physician', true
FROM professional_user;

-- Insert admin user (no additional profile needed)
INSERT INTO users (email, password_hash, user_type) 
VALUES ('admin@example.com', 'TODO_IMPLEMENT_PASSWORD_HASHING', 'admin');