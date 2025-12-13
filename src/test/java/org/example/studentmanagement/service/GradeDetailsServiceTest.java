package org.example.studentmanagement.service;

import java.util.Arrays;
import java.util.List;

import org.example.studentmanagement.dao.GradeDetailsDAO;
import org.example.studentmanagement.entity.GradeDetails;
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

class GradeDetailsServiceTest {

    private GradeDetailsServiceImpl gradeDetailsService;
    private GradeDetailsDAO gradeDetailsDAO;

    @BeforeEach
    void setUp() {
        gradeDetailsDAO = mock(GradeDetailsDAO.class);
        gradeDetailsService = new GradeDetailsServiceImpl(gradeDetailsDAO);
    }

    @Test
    void findById_returnsGradeDetailsWhenExists() {
        // Arrange
        GradeDetails gradeDetails = new GradeDetails(1, 100, 200, "Assignment 1", 
                85.0, 100.0, "B", "Good work", "2024-01-15", 300);
        when(gradeDetailsDAO.findById(1)).thenReturn(gradeDetails);

        // Act
        GradeDetails result = gradeDetailsService.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(100, result.getStudentId());
        assertEquals(200, result.getCourseId());
        assertEquals("Assignment 1", result.getAssignmentName());
        assertEquals(85.0, result.getScore());
        assertEquals(100.0, result.getMaxScore());
        assertEquals("B", result.getGrade());
        verify(gradeDetailsDAO).findById(1);
    }

    @Test
    void findById_returnsNullWhenNotFound() {
        // Arrange
        when(gradeDetailsDAO.findById(999)).thenReturn(null);

        // Act
        GradeDetails result = gradeDetailsService.findById(999);

        // Assert
        assertNull(result);
        verify(gradeDetailsDAO).findById(999);
    }

    @Test
    void findAll_returnsAllGradeDetails() {
        // Arrange
        GradeDetails grade1 = new GradeDetails(1, 100, 200, "Assignment 1", 
                85.0, 100.0, "B", "Good work", "2024-01-15", 300);
        GradeDetails grade2 = new GradeDetails(2, 100, 200, "Assignment 2", 
                92.0, 100.0, "A", "Excellent", "2024-01-20", 300);
        GradeDetails grade3 = new GradeDetails(3, 101, 200, "Assignment 1", 
                78.0, 100.0, "C", "Needs improvement", "2024-01-15", 300);
        List<GradeDetails> gradeDetailsList = Arrays.asList(grade1, grade2, grade3);
        when(gradeDetailsDAO.findAll()).thenReturn(gradeDetailsList);

        // Act
        List<GradeDetails> result = gradeDetailsService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(grade1, result.get(0));
        assertEquals(grade2, result.get(1));
        assertEquals(grade3, result.get(2));
        verify(gradeDetailsDAO).findAll();
    }

    @Test
    void findAll_returnsEmptyListWhenNoGradeDetails() {
        // Arrange
        when(gradeDetailsDAO.findAll()).thenReturn(Arrays.asList());

        // Act
        List<GradeDetails> result = gradeDetailsService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(gradeDetailsDAO).findAll();
    }

    @Test
    void save_delegatesToDAO() {
        // Arrange
        GradeDetails gradeDetails = new GradeDetails(1, 100, 200, "Assignment 1", 
                85.0, 100.0, "B", "Good work", "2024-01-15", 300);

        // Act
        gradeDetailsService.save(gradeDetails);

        // Assert
        verify(gradeDetailsDAO).save(gradeDetails);
    }

    @Test
    void save_handlesNewGradeDetails() {
        // Arrange
        GradeDetails newGrade = new GradeDetails(0, 100, 200, "New Assignment", 
                0.0, 100.0, null, null, null, 300);

        // Act
        gradeDetailsService.save(newGrade);

        // Assert
        verify(gradeDetailsDAO).save(newGrade);
        verify(gradeDetailsDAO, never()).findById(anyInt());
    }

    @Test
    void save_handlesExistingGradeDetails() {
        // Arrange
        GradeDetails existingGrade = new GradeDetails(1, 100, 200, "Assignment 1", 
                90.0, 100.0, "A", "Updated feedback", "2024-01-15", 300);

        // Act
        gradeDetailsService.save(existingGrade);

        // Assert
        verify(gradeDetailsDAO).save(existingGrade);
    }

    @Test
    void save_handlesGradeDetailsWithDifferentGrades() {
        // Arrange
        GradeDetails gradeA = new GradeDetails(1, 100, 200, "Assignment 1", 
                95.0, 100.0, "A", "Excellent", "2024-01-15", 300);
        GradeDetails gradeF = new GradeDetails(2, 100, 200, "Assignment 2", 
                45.0, 100.0, "F", "Failed", "2024-01-15", 300);

        // Act
        gradeDetailsService.save(gradeA);
        gradeDetailsService.save(gradeF);

        // Assert
        verify(gradeDetailsDAO).save(gradeA);
        verify(gradeDetailsDAO).save(gradeF);
        assertEquals("A", gradeA.getGrade());
        assertEquals("F", gradeF.getGrade());
    }

    @Test
    void deleteById_delegatesToDAO() {
        // Act
        gradeDetailsService.deleteById(1);

        // Assert
        verify(gradeDetailsDAO).deleteById(1);
    }

    @Test
    void deleteById_handlesNonExistentId() {
        // Act
        gradeDetailsService.deleteById(999);

        // Assert
        verify(gradeDetailsDAO).deleteById(999);
    }

    @Test
    void allMethods_delegateToDAO() {
        // Arrange
        GradeDetails gradeDetails = new GradeDetails(1, 100, 200, "Assignment 1", 
                85.0, 100.0, "B", "Good work", "2024-01-15", 300);
        when(gradeDetailsDAO.findById(1)).thenReturn(gradeDetails);
        when(gradeDetailsDAO.findAll()).thenReturn(Arrays.asList(gradeDetails));

        // Act & Assert
        gradeDetailsService.findById(1);
        verify(gradeDetailsDAO).findById(1);

        gradeDetailsService.findAll();
        verify(gradeDetailsDAO).findAll();

        gradeDetailsService.save(gradeDetails);
        verify(gradeDetailsDAO).save(gradeDetails);

        gradeDetailsService.deleteById(1);
        verify(gradeDetailsDAO).deleteById(1);
    }

    @Test
    void findById_handlesGradeDetailsWithPercentage() {
        // Arrange
        GradeDetails gradeDetails = new GradeDetails(1, 100, 200, "Assignment 1", 
                85.0, 100.0, "B", "Good work", "2024-01-15", 300);
        when(gradeDetailsDAO.findById(1)).thenReturn(gradeDetails);

        // Act
        GradeDetails result = gradeDetailsService.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals(85.0, result.getPercentage());
    }
}

