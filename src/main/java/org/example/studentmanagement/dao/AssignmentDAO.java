package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.Assignment;

import java.util.List;

public interface AssignmentDAO extends BaseDAO<Assignment, Integer> {
    // Custom methods for Assignment
    List<Assignment> findByCourseId(int courseId);
    List<Assignment> findByStatus(String status);
    List<Assignment> findByCourseIdAndStatus(int courseId, String status);
}

