package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.StudentCourseDetails;
import java.util.List;

public interface StudentCourseDetailsService {
    StudentCourseDetails findByStudentAndCourseId(int studentId, int courseId);
    List<StudentCourseDetails> findAll();
    void save(StudentCourseDetails studentCourseDetails);
    void deleteById(int id);
}

