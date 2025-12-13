package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.StudentCourseDetails;

import java.util.List;

public interface StudentCourseDetailsDAO extends BaseDAO<StudentCourseDetails, Integer> {
    // Additional custom methods for StudentCourseDetails
    List<StudentCourseDetails> findByStudentId(int studentId);
    List<StudentCourseDetails> findByCourseId(int courseId);
    StudentCourseDetails findByStudentIdAndCourseId(int studentId, int courseId);
}

