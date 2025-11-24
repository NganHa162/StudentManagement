package org.example.studentmanagement.repository;

import java.util.List;

import org.example.studentmanagement.entity.StudentCourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCourseEnrollmentRepository extends JpaRepository<StudentCourseEnrollment, Long> {

    List<StudentCourseEnrollment> findByStudentId(Long studentId);
}

