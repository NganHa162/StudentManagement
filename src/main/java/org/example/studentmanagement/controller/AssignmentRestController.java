package org.example.studentmanagement.controller;

import java.util.List;
import java.util.Optional;

import org.example.studentmanagement.dto.StudentAssignmentResponse;
import org.example.studentmanagement.security.StudentPrincipal;
import org.example.studentmanagement.service.StudentAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students/me")
public class AssignmentRestController {

    private final StudentAssignmentService studentAssignmentService;

    public AssignmentRestController(StudentAssignmentService studentAssignmentService) {
        this.studentAssignmentService = studentAssignmentService;
    }

    @GetMapping("/assignments")
    public ResponseEntity<List<StudentAssignmentResponse>> getAssignments(
            @RequestParam(value = "courseName", required = false) String courseName,
            Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof StudentPrincipal principal)) {
            return ResponseEntity.status(403).build();
        }
        List<StudentAssignmentResponse> assignments = studentAssignmentService
                .getAssignmentsForStudent(principal.getStudentId(), Optional.ofNullable(courseName));
        return ResponseEntity.ok(assignments);
    }
}

