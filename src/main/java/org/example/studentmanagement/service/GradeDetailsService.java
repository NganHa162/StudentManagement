package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.GradeDetails;
import java.util.List;

public interface GradeDetailsService {
    GradeDetails findById(int id);
    List<GradeDetails> findAll();
    void save(GradeDetails gradeDetails);
    void deleteById(int id);
    List<GradeDetails> findByStudentIdAndCourseId(int studentId, int courseId);
}

