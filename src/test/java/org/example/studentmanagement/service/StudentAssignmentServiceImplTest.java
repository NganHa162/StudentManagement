package org.example.studentmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.example.studentmanagement.dto.StudentAssignmentResponse;
import org.example.studentmanagement.dto.StudentAssignmentRow;
import org.example.studentmanagement.repository.AssignmentDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentAssignmentServiceImplTest {

    @Mock
    private AssignmentDetailsRepository assignmentDetailsRepository;

    private StudentAssignmentServiceImpl studentAssignmentService;

    @BeforeEach
    void setUp() {
        studentAssignmentService = new StudentAssignmentServiceImpl(assignmentDetailsRepository);
    }

    @Test
    void shouldMapRowsToResponses() {
        LocalDate dueDate = LocalDate.now().plusDays(5);
        StudentAssignmentRow row = new StudentAssignmentRow(1L, "Assignment", "Desc", dueDate, "Math", false);
        when(assignmentDetailsRepository.findAssignmentsForStudent(any(), any()))
                .thenReturn(List.of(row));

        List<StudentAssignmentResponse> responses = studentAssignmentService
                .getAssignmentsForStudent(1L, Optional.empty());

        assertThat(responses).hasSize(1);
        StudentAssignmentResponse response = responses.get(0);
        assertThat(response.getAssignmentId()).isEqualTo(1L);
        assertThat(response.getCourseName()).isEqualTo("Math");
        assertThat(response.getStatus()).isEqualTo("PENDING");
        assertThat(response.getDaysRemaining()).isEqualTo(5);
    }

    @Test
    void shouldPassCourseFilter() {
        when(assignmentDetailsRepository.findAssignmentsForStudent(1L, "science"))
                .thenReturn(List.of());

        studentAssignmentService.getAssignmentsForStudent(1L, Optional.of("science"));

        // interaction verified via when/thenReturn; no exception equals success
    }
}

