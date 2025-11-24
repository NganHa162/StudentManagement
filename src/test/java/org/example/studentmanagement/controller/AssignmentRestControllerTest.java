package org.example.studentmanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.example.studentmanagement.dto.StudentAssignmentResponse;
import org.example.studentmanagement.entity.Role;
import org.example.studentmanagement.entity.Student;
import org.example.studentmanagement.security.StudentPrincipal;
import org.example.studentmanagement.service.StudentAssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

class AssignmentRestControllerTest {

    @Mock
    private StudentAssignmentService studentAssignmentService;

    @Mock
    private Authentication authentication;

    private AssignmentRestController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new AssignmentRestController(studentAssignmentService);
    }

    @Test
    void shouldReturnAssignmentsForAuthenticatedStudent() {
        Student student = new Student();
        student.setId(10L);
        Role role = new Role();
        role.setName("ROLE_STUDENT");
        student.setRole(role);
        StudentPrincipal principal = new StudentPrincipal(student);

        when(authentication.getPrincipal()).thenReturn(principal);

        StudentAssignmentResponse responseDto = new StudentAssignmentResponse();
        responseDto.setAssignmentId(5L);
        responseDto.setTitle("Essay");
        responseDto.setDueDate(LocalDate.now());
        when(studentAssignmentService.getAssignmentsForStudent(eq(10L), eq(Optional.of("math"))))
                .thenReturn(List.of(responseDto));

        ResponseEntity<List<StudentAssignmentResponse>> response = controller.getAssignments("math", authentication);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getAssignmentId()).isEqualTo(5L);
    }

    @Test
    void shouldReturnForbiddenWhenPrincipalMissing() {
        when(authentication.getPrincipal()).thenReturn("unknown");

        ResponseEntity<List<StudentAssignmentResponse>> response = controller.getAssignments(null, authentication);

        assertThat(response.getStatusCode().value()).isEqualTo(403);
    }
}

