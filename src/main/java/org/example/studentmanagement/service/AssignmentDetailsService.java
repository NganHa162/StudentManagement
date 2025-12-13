package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.AssignmentDetails;
import java.util.List;

public interface AssignmentDetailsService {
    AssignmentDetails findByAssignmentAndStudentCourseDetailsId(int assignmentId, int studentCourseDetailsId);
    List<AssignmentDetails> findAll();
    void save(AssignmentDetails assignmentDetails);
    void deleteById(int id);
}

