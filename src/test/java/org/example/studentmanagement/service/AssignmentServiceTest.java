package org.example.studentmanagement.service;

import java.util.Arrays;
import java.util.List;

import org.example.studentmanagement.dao.AssignmentDAO;
import org.example.studentmanagement.entity.Assignment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AssignmentServiceTest {

    private AssignmentServiceImpl assignmentService;
    private AssignmentDAO assignmentDAO;

    @BeforeEach
    void setUp() {
        assignmentDAO = mock(AssignmentDAO.class);
        assignmentService = new AssignmentServiceImpl(assignmentDAO);
    }

    @Test
    void findById_returnsAssignmentWhenExists() {
        // Arrange
        Assignment assignment = new Assignment(1, 100, "Test Assignment", "Description", 
                "2024-12-31", 100.0, "2024-01-01", "active", 200);
        when(assignmentDAO.findById(1)).thenReturn(assignment);

        // Act
        Assignment result = assignmentService.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test Assignment", result.getTitle());
        assertEquals(100, result.getCourseId());
        verify(assignmentDAO).findById(1);
    }

    @Test
    void findById_returnsNullWhenNotFound() {
        // Arrange
        when(assignmentDAO.findById(999)).thenReturn(null);

        // Act
        Assignment result = assignmentService.findById(999);

        // Assert
        assertNull(result);
        verify(assignmentDAO).findById(999);
    }

    @Test
    void findAll_returnsAllAssignments() {
        // Arrange
        Assignment assignment1 = new Assignment(1, 100, "Assignment 1", "Description 1", 
                "2024-12-31", 100.0, "2024-01-01", "active", 200);
        Assignment assignment2 = new Assignment(2, 100, "Assignment 2", "Description 2", 
                "2024-12-31", 100.0, "2024-01-01", "active", 200);
        List<Assignment> assignments = Arrays.asList(assignment1, assignment2);
        when(assignmentDAO.findAll()).thenReturn(assignments);

        // Act
        List<Assignment> result = assignmentService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(assignment1, result.get(0));
        assertEquals(assignment2, result.get(1));
        verify(assignmentDAO).findAll();
    }

    @Test
    void findAll_returnsEmptyListWhenNoAssignments() {
        // Arrange
        when(assignmentDAO.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Assignment> result = assignmentService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(assignmentDAO).findAll();
    }

    @Test
    void save_delegatesToDAO() {
        // Arrange
        Assignment assignment = new Assignment(1, 100, "New Assignment", "Description", 
                "2024-12-31", 100.0, "2024-01-01", "active", 200);

        // Act
        assignmentService.save(assignment);

        // Assert
        verify(assignmentDAO).save(assignment);
    }

    @Test
    void save_handlesNewAssignment() {
        // Arrange
        Assignment newAssignment = new Assignment(0, 100, "New Assignment", "Description", 
                "2024-12-31", 100.0, "2024-01-01", "draft", 200);

        // Act
        assignmentService.save(newAssignment);

        // Assert
        verify(assignmentDAO).save(newAssignment);
        verify(assignmentDAO, never()).findById(anyInt());
    }

    @Test
    void save_handlesExistingAssignment() {
        // Arrange
        Assignment existingAssignment = new Assignment(1, 100, "Updated Assignment", "Updated Description", 
                "2024-12-31", 100.0, "2024-01-01", "active", 200);

        // Act
        assignmentService.save(existingAssignment);

        // Assert
        verify(assignmentDAO).save(existingAssignment);
    }

    @Test
    void deleteAssignmentById_delegatesToDAO() {
        // Act
        assignmentService.deleteAssignmentById(1);

        // Assert
        verify(assignmentDAO).deleteById(1);
    }

    @Test
    void deleteAssignmentById_handlesNonExistentId() {
        // Act
        assignmentService.deleteAssignmentById(999);

        // Assert
        verify(assignmentDAO).deleteById(999);
    }

    @Test
    void allMethods_delegateToDAO() {
        // Arrange
        Assignment assignment = new Assignment(1, 100, "Test", "Desc", 
                "2024-12-31", 100.0, "2024-01-01", "active", 200);
        when(assignmentDAO.findById(1)).thenReturn(assignment);
        when(assignmentDAO.findAll()).thenReturn(Arrays.asList(assignment));

        // Act & Assert
        assignmentService.findById(1);
        verify(assignmentDAO).findById(1);

        assignmentService.findAll();
        verify(assignmentDAO).findAll();

        assignmentService.save(assignment);
        verify(assignmentDAO).save(assignment);

        assignmentService.deleteAssignmentById(1);
        verify(assignmentDAO).deleteById(1);
    }
}

