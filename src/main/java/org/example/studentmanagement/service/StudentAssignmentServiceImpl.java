package org.example.studentmanagement.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.studentmanagement.dto.StudentAssignmentResponse;
import org.example.studentmanagement.dto.StudentAssignmentRow;
import org.example.studentmanagement.repository.AssignmentDetailsRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentAssignmentServiceImpl implements StudentAssignmentService {

    private final AssignmentDetailsRepository assignmentDetailsRepository;

    public StudentAssignmentServiceImpl(AssignmentDetailsRepository assignmentDetailsRepository) {
        this.assignmentDetailsRepository = assignmentDetailsRepository;
    }

    @Override
    public List<StudentAssignmentResponse> getAssignmentsForStudent(Long studentId, Optional<String> courseNameFilter) {
        List<StudentAssignmentRow> rows = assignmentDetailsRepository
                .findAssignmentsForStudent(studentId, courseNameFilter.filter(s -> !s.isBlank()).orElse(null));

        return rows.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private StudentAssignmentResponse toResponse(StudentAssignmentRow row) {
        StudentAssignmentResponse response = new StudentAssignmentResponse();
        response.setAssignmentId(row.getAssignmentId());
        response.setTitle(row.getTitle());
        response.setDescription(row.getDescription());
        response.setDueDate(row.getDueDate());
        response.setCourseName(row.getCourseName());
        response.setStatus(row.isDone() ? "COMPLETED" : "PENDING");
        response.setDaysRemaining(calculateDaysRemaining(row.getDueDate()));
        return response;
    }

    private int calculateDaysRemaining(LocalDate dueDate) {
        if (dueDate == null) {
            return -1;
        }
        long days = ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
        return (int) days;
    }
}

