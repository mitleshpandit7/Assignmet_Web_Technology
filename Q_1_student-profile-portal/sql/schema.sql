-- =====================================================================
--  Student Profile Portal - database schema
--  Run FIRST.   mysql -u root -p < sql/schema.sql
--  Safe to re-run: nothing is dropped or overwritten.
-- =====================================================================
CREATE DATABASE IF NOT EXISTS student_portal
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE student_portal;

-- People who can log in to the portal.
CREATE TABLE IF NOT EXISTS users (
    id            INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    username      VARCHAR(50)   NOT NULL,
    password_hash VARCHAR(255)  NOT NULL,   -- PBKDF2 hash, never a plain-text password
    full_name     VARCHAR(100)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_users_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- The student records shown in the portal.
-- image_path / document_path = file name (or sub-path) RELATIVE to the upload.dir folder.
CREATE TABLE IF NOT EXISTS students (
    id            INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    name          VARCHAR(100)  NOT NULL,
    email         VARCHAR(150)  NOT NULL,
    department    VARCHAR(100)  NOT NULL,
    image_path    VARCHAR(255)  NULL,
    document_path VARCHAR(255)  NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_students_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
