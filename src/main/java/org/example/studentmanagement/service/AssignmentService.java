package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.Assignment;
import java.util.List;

public interface AssignmentService {
    Assignment findById(int id);

    List<Assignment> findAll();

    List<Assignment> findByCourseId(int courseId);

    void save(Assignment assignment);

    void deleteAssignmentById(int id);
}
