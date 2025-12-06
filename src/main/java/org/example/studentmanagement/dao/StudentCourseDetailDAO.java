package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.StudentCourseDetail;

import java.util.List;

public interface StudentCourseDetailDAO extends BaseDAO<StudentCourseDetail, Integer> {
    // Additional custom methods for StudentCourseDetail
    List<StudentCourseDetail> findByStudentId(int studentId);
    List<StudentCourseDetail> findByCourseId(int courseId);
}
