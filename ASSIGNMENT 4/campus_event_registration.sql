CREATE DATABASE IF NOT EXISTS campus_events CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus_events;

CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(20) PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    department VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    INDEX idx_student_name (student_name)
);

CREATE TABLE IF NOT EXISTS events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(150) NOT NULL UNIQUE,
    event_date DATE NULL,
    INDEX idx_event_name (event_name)
);

CREATE TABLE IF NOT EXISTS committee_users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS registrations (
    registration_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    event_id INT NOT NULL,
    registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_student_event UNIQUE (student_id, event_id),
    CONSTRAINT fk_registration_student FOREIGN KEY (student_id) REFERENCES students(student_id),
    CONSTRAINT fk_registration_event FOREIGN KEY (event_id) REFERENCES events(event_id),
    INDEX idx_registration_event (event_id),
    INDEX idx_registration_student (student_id)
);

INSERT IGNORE INTO events(event_name,event_date) VALUES
 ('Tech Symposium','2026-12-10'), ('Cultural Fest','2026-12-18'),
 ('Sports Meet','2027-01-15'), ('Hackathon','2027-02-05');

-- Generate a real bcrypt/Argon hash for production. This demo password is SHA-256 and is not suitable for real deployment.
INSERT IGNORE INTO committee_users(username,password_hash) VALUES
 ('committee','CHANGE_ME_TO_A_PASSWORD_HASH');
