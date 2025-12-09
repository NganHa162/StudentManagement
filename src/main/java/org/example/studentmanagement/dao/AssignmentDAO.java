package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.Assignment;

import java.util.List;

public interface AssignmentDAO extends BaseDAO<Assignment, Integer> {
    List<Assignment> findByCourseId(int courseId);
}

