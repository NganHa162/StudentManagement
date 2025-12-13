package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.AssignmentDetails;

import java.util.List;

public interface AssignmentDetailsDAO extends BaseDAO<AssignmentDetails, Integer> {
    AssignmentDetails findByAssignmentIdAndStudentCourseDetailsId(int assignmentId, int studentCourseDetailsId);
    List<AssignmentDetails> findByAssignmentId(int assignmentId);
    List<AssignmentDetails> findByStudentCourseDetailsId(int studentCourseDetailsId);
}

