CREATE DATABASE event_db;
USE event_db;

CREATE TABLE registrations (
    registration_id INT PRIMARY KEY AUTO_INCREMENT,
    participant_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    event_name VARCHAR(100) NOT NULL,
    registration_date DATE NOT NULL,
    usn VARCHAR(30) NOT NULL,
    course VARCHAR(50),
    semester INT
);

DESCRIBE registrations;
SHOW TABLES;
SELECT * FROM registrations;
