package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.GradeDetails;

import java.util.List;

public interface GradeDetailsDAO extends BaseDAO<GradeDetails, Integer> {
    // Custom methods for GradeDetails
    List<GradeDetails> findByStudentId(int studentId);
    List<GradeDetails> findByCourseId(int courseId);
    List<GradeDetails> findByStudentIdAndCourseId(int studentId, int courseId);
}

