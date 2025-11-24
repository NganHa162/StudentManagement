package org.example.studentmanagement.service;

import java.util.List;
import java.util.Optional;

import org.example.studentmanagement.dto.StudentAssignmentResponse;

public interface StudentAssignmentService {

    List<StudentAssignmentResponse> getAssignmentsForStudent(Long studentId, Optional<String> courseNameFilter);
}

