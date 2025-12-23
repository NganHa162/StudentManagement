-- Sample data for testing the View My Course use case
-- This file will be executed automatically by Spring Boot on startup

-- Insert sample teachers
INSERT INTO teachers (teacher_code, full_name, email, department) 
VALUES 
    ('T001', 'Dr. Nguyen Van A', 'nguyenvana@university.edu', 'Computer Science'),
    ('T002', 'Prof. Tran Thi B', 'tranthib@university.edu', 'Mathematics'),
    ('T003', 'Dr. Le Van C', 'levanc@university.edu', 'Physics')
ON CONFLICT (teacher_code) DO NOTHING;

-- Insert sample students
INSERT INTO students (student_code, full_name, email, date_of_birth) 
VALUES 
    ('S001', 'Hoang Nguyen Trong', 'hoang.student@university.edu', '2002-05-15'),
    ('S002', 'Minh Pham Van', 'minh.student@university.edu', '2001-08-20'),
    ('S003', 'Linh Vo Thi', 'linh.student@university.edu', '2002-03-10')
ON CONFLICT (student_code) DO NOTHING;

-- Insert sample courses
INSERT INTO courses (course_code, course_name, schedule, teacher_id) 
VALUES 
    ('CS101', 'Introduction to Programming', 'Mon-Wed 9:00-11:00', (SELECT teacher_id FROM teachers WHERE teacher_code = 'T001')),
    ('CS102', 'Data Structures', 'Tue-Thu 13:00-15:00', (SELECT teacher_id FROM teachers WHERE teacher_code = 'T001')),
    ('MATH201', 'Calculus I', 'Mon-Wed-Fri 7:30-9:00', (SELECT teacher_id FROM teachers WHERE teacher_code = 'T002')),
    ('PHYS101', 'General Physics', 'Tue-Thu 15:30-17:00', (SELECT teacher_id FROM teachers WHERE teacher_code = 'T003')),
    ('CS201', 'Algorithms', 'Wed-Fri 10:00-12:00', (SELECT teacher_id FROM teachers WHERE teacher_code = 'T001'))
ON CONFLICT (course_code) DO NOTHING;

-- Enroll students in courses
INSERT INTO student_course_details (student_id, course_id, enrollment_date, status) 
VALUES 
    -- Student S001 enrollments
    ((SELECT student_id FROM students WHERE student_code = 'S001'), 
     (SELECT course_id FROM courses WHERE course_code = 'CS101'), 
     '2024-09-01', 'ACTIVE'),
    ((SELECT student_id FROM students WHERE student_code = 'S001'), 
     (SELECT course_id FROM courses WHERE course_code = 'CS102'), 
     '2024-09-01', 'ACTIVE'),
    ((SELECT student_id FROM students WHERE student_code = 'S001'), 
     (SELECT course_id FROM courses WHERE course_code = 'MATH201'), 
     '2024-09-01', 'ACTIVE'),
    
    -- Student S002 enrollments
    ((SELECT student_id FROM students WHERE student_code = 'S002'), 
     (SELECT course_id FROM courses WHERE course_code = 'CS101'), 
     '2024-09-01', 'ACTIVE'),
    ((SELECT student_id FROM students WHERE student_code = 'S002'), 
     (SELECT course_id FROM courses WHERE course_code = 'PHYS101'), 
     '2024-09-01', 'ACTIVE'),
    
    -- Student S003 enrollments
    ((SELECT student_id FROM students WHERE student_code = 'S003'), 
     (SELECT course_id FROM courses WHERE course_code = 'CS201'), 
     '2024-09-01', 'ACTIVE'),
    ((SELECT student_id FROM students WHERE student_code = 'S003'), 
     (SELECT course_id FROM courses WHERE course_code = 'MATH201'), 
     '2024-09-01', 'COMPLETED')
ON CONFLICT DO NOTHING;

