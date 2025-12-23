package org.example.studentmanagement.repository;

import org.example.studentmanagement.entity.StudentCourseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseDetailsRepository extends JpaRepository<StudentCourseDetails, Integer> {
    
    List<StudentCourseDetails> findByStudentStudentId(Integer studentId);
    
    List<StudentCourseDetails> findByCourseCourseId(Integer courseId);
    
    List<StudentCourseDetails> findByStudentStudentIdAndStatus(Integer studentId, String status);
}

