-- Student Management System Database Initialization
-- PostgreSQL 15+
-- This script creates all tables and inserts sample data

-- Drop existing tables (in correct order to respect foreign keys)
DROP TABLE IF EXISTS assignment_details CASCADE;
DROP TABLE IF EXISTS grade_details CASCADE;
DROP TABLE IF EXISTS assignments CASCADE;
DROP TABLE IF EXISTS student_course_details CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS teachers CASCADE;
DROP TABLE IF EXISTS admins CASCADE;

-- 1. Admin Table
CREATE TABLE admins (
    id SERIAL PRIMARY KEY,
    user_name VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- 2. Student Table
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    user_name VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- 3. Teacher Table
CREATE TABLE teachers (
    id SERIAL PRIMARY KEY,
    user_name VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- 4. Course Table
CREATE TABLE courses (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    teacher_id INTEGER REFERENCES teachers(id) ON DELETE SET NULL
);

-- 5. Student Course Details (Junction table for students and courses)
CREATE TABLE student_course_details (
    id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    course_id INTEGER NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    UNIQUE(student_id, course_id)
);

-- 6. Assignment Table
CREATE TABLE assignments (
    id SERIAL PRIMARY KEY,
    course_id INTEGER NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    due_date VARCHAR(50),
    max_score DECIMAL(5,2) DEFAULT 100.00,
    created_date VARCHAR(50),
    status VARCHAR(20) DEFAULT 'active', -- 'active', 'closed', 'draft'
    created_by_teacher_id INTEGER REFERENCES teachers(id) ON DELETE SET NULL
);

-- 7. Assignment Details (tracks student assignment completion)
CREATE TABLE assignment_details (
    id SERIAL PRIMARY KEY,
    assignment_id INTEGER NOT NULL REFERENCES assignments(id) ON DELETE CASCADE,
    student_course_details_id INTEGER NOT NULL REFERENCES student_course_details(id) ON DELETE CASCADE,
    is_done INTEGER DEFAULT 0, -- 0 = incomplete, 1 = completed
    UNIQUE(assignment_id, student_course_details_id)
);

-- 8. Grade Details Table
CREATE TABLE grade_details (
    id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    course_id INTEGER NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    assignment_name VARCHAR(200),
    score DECIMAL(5,2) DEFAULT 0.00,
    max_score DECIMAL(5,2) DEFAULT 100.00,
    grade VARCHAR(2), -- 'A', 'B', 'C', 'D', 'F'
    feedback TEXT,
    graded_date VARCHAR(50),
    graded_by_teacher_id INTEGER REFERENCES teachers(id) ON DELETE SET NULL
);

-- Create indexes for better query performance
CREATE INDEX idx_course_teacher ON courses(teacher_id);
CREATE INDEX idx_student_course_student ON student_course_details(student_id);
CREATE INDEX idx_student_course_course ON student_course_details(course_id);
CREATE INDEX idx_assignment_course ON assignments(course_id);
CREATE INDEX idx_grade_student ON grade_details(student_id);
CREATE INDEX idx_grade_course ON grade_details(course_id);

-- ========================================
-- INSERT SAMPLE DATA
-- ========================================

-- Sample Admin Account
-- Login with: admin@example.com / admin123 OR admin / admin123
INSERT INTO admins (user_name, password, first_name, last_name, email)
VALUES ('admin', 'admin123', 'Admin', 'User', 'admin@example.com');

-- Sample Students
-- Login with email (student1@example.com / student123) OR username (student11 / student123)
INSERT INTO students (user_name, password, first_name, last_name, email) VALUES
('student11', 'student123', 'Nguyen', 'Van A', 'student1@example.com'),
('student21', 'student123', 'Tran', 'Thi B', 'student2@example.com'),
('student31', 'student123', 'Le', 'Van C', 'student3@example.com');

-- Sample Teachers
-- Login with email (teacher1@example.com / teacher123) OR username (teacher11 / teacher123)
INSERT INTO teachers (user_name, password, first_name, last_name, email) VALUES
('teacher11', 'teacher123', 'Pham', 'Thi D', 'teacher1@example.com'),
('teacher21', 'teacher123', 'Hoang', 'Van E', 'teacher2@example.com');

-- Sample Courses
INSERT INTO courses (code, name, teacher_id) VALUES
('CS101', 'Introduction to Programming', 1),
('CS102', 'Data Structures', 1),
('MATH101', 'Calculus I', 2);

-- Enroll students in courses
INSERT INTO student_course_details (student_id, course_id) VALUES
(1, 1), -- Student 1 in CS101
(1, 2), -- Student 1 in CS102
(2, 1), -- Student 2 in CS101
(2, 3), -- Student 2 in MATH101
(3, 2), -- Student 3 in CS102
(3, 3); -- Student 3 in MATH101

-- Sample Assignments
INSERT INTO assignments (course_id, title, description, due_date, max_score, created_date, status, created_by_teacher_id) VALUES
(1, 'Assignment 1: Hello World', 'Write your first program', '2026-02-15', 100, '2026-01-01', 'active', 1),
(1, 'Assignment 2: Variables', 'Learn about variables and data types', '2026-02-22', 100, '2026-01-08', 'active', 1),
(2, 'Assignment 1: Arrays', 'Implement array operations', '2026-02-20', 100, '2026-01-05', 'active', 1),
(3, 'Assignment 1: Derivatives', 'Calculate derivatives', '2026-02-18', 100, '2026-01-03', 'active', 2);

-- Track assignment completion (all initially incomplete)
-- For CS101 students
INSERT INTO assignment_details (assignment_id, student_course_details_id, is_done) VALUES
(1, 1, 0), -- Student 1, CS101, Assignment 1
(2, 1, 0), -- Student 1, CS101, Assignment 2
(1, 3, 1), -- Student 2, CS101, Assignment 1 (completed)
(2, 3, 0); -- Student 2, CS101, Assignment 2

-- For CS102 students
INSERT INTO assignment_details (assignment_id, student_course_details_id, is_done) VALUES
(3, 2, 0), -- Student 1, CS102, Assignment 1
(3, 5, 0); -- Student 3, CS102, Assignment 1

-- For MATH101 students
INSERT INTO assignment_details (assignment_id, student_course_details_id, is_done) VALUES
(4, 4, 0), -- Student 2, MATH101, Assignment 1
(4, 6, 1); -- Student 3, MATH101, Assignment 1 (completed)

-- Sample Grades
INSERT INTO grade_details (student_id, course_id, assignment_name, score, max_score, grade, feedback, graded_date, graded_by_teacher_id) VALUES
(1, 1, 'Mid-term Exam', 95.0, 100.0, 'A', 'Excellent work!', '2026-01-16', 1),
(2, 1, 'Mid-term Exam', 88.0, 100.0, 'B', 'Good job!', '2026-01-16', 1),
(3, 3, 'Mid-term Exam', 92.0, 100.0, 'A', 'Well done!', '2026-01-19', 2);

-- Verification queries
SELECT 'Admin count:' as info, COUNT(*) as count FROM admins
UNION ALL
SELECT 'Student count:', COUNT(*) FROM students
UNION ALL
SELECT 'Teacher count:', COUNT(*) FROM teachers
UNION ALL
SELECT 'Course count:', COUNT(*) FROM courses
UNION ALL
SELECT 'Enrollment count:', COUNT(*) FROM student_course_details
UNION ALL
SELECT 'Assignment count:', COUNT(*) FROM assignments
UNION ALL
SELECT 'Grade count:', COUNT(*) FROM grade_details;

COMMIT;
