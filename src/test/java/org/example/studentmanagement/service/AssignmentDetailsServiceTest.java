package org.example.studentmanagement.service;

import java.util.Arrays;
import java.util.List;

import org.example.studentmanagement.dao.AssignmentDetailsDAO;
import org.example.studentmanagement.entity.AssignmentDetails;
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

class AssignmentDetailsServiceTest {

    private AssignmentDetailsServiceImpl assignmentDetailsService;
    private AssignmentDetailsDAO assignmentDetailsDAO;

    @BeforeEach
    void setUp() {
        assignmentDetailsDAO = mock(AssignmentDetailsDAO.class);
        assignmentDetailsService = new AssignmentDetailsServiceImpl(assignmentDetailsDAO);
    }

    @Test
    void findByAssignmentAndStudentCourseDetailsId_returnsAssignmentDetailsWhenExists() {
        // Arrange
        AssignmentDetails assignmentDetails = new AssignmentDetails(1, 100, 200, 1);
        when(assignmentDetailsDAO.findByAssignmentIdAndStudentCourseDetailsId(100, 200))
                .thenReturn(assignmentDetails);

        // Act
        AssignmentDetails result = assignmentDetailsService.findByAssignmentAndStudentCourseDetailsId(100, 200);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(100, result.getAssignmentId());
        assertEquals(200, result.getStudentCourseDetailsId());
        assertEquals(1, result.getIsDone());
        verify(assignmentDetailsDAO).findByAssignmentIdAndStudentCourseDetailsId(100, 200);
    }

    @Test
    void findByAssignmentAndStudentCourseDetailsId_returnsNullWhenNotFound() {
        // Arrange
        when(assignmentDetailsDAO.findByAssignmentIdAndStudentCourseDetailsId(999, 888))
                .thenReturn(null);

        // Act
        AssignmentDetails result = assignmentDetailsService.findByAssignmentAndStudentCourseDetailsId(999, 888);

        // Assert
        assertNull(result);
        verify(assignmentDetailsDAO).findByAssignmentIdAndStudentCourseDetailsId(999, 888);
    }

    @Test
    void findAll_returnsAllAssignmentDetails() {
        // Arrange
        AssignmentDetails details1 = new AssignmentDetails(1, 100, 200, 1);
        AssignmentDetails details2 = new AssignmentDetails(2, 100, 201, 0);
        AssignmentDetails details3 = new AssignmentDetails(3, 101, 200, 1);
        List<AssignmentDetails> assignmentDetailsList = Arrays.asList(details1, details2, details3);
        when(assignmentDetailsDAO.findAll()).thenReturn(assignmentDetailsList);

        // Act
        List<AssignmentDetails> result = assignmentDetailsService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(details1, result.get(0));
        assertEquals(details2, result.get(1));
        assertEquals(details3, result.get(2));
        verify(assignmentDetailsDAO).findAll();
    }

    @Test
    void findAll_returnsEmptyListWhenNoAssignmentDetails() {
        // Arrange
        when(assignmentDetailsDAO.findAll()).thenReturn(Arrays.asList());

        // Act
        List<AssignmentDetails> result = assignmentDetailsService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(assignmentDetailsDAO).findAll();
    }

    @Test
    void save_delegatesToDAO() {
        // Arrange
        AssignmentDetails assignmentDetails = new AssignmentDetails(1, 100, 200, 1);

        // Act
        assignmentDetailsService.save(assignmentDetails);

        // Assert
        verify(assignmentDetailsDAO).save(assignmentDetails);
    }

    @Test
    void save_handlesNewAssignmentDetails() {
        // Arrange
        AssignmentDetails newDetails = new AssignmentDetails(0, 100, 200, 0);

        // Act
        assignmentDetailsService.save(newDetails);

        // Assert
        verify(assignmentDetailsDAO).save(newDetails);
        verify(assignmentDetailsDAO, never()).findById(anyInt());
    }

    @Test
    void save_handlesExistingAssignmentDetails() {
        // Arrange
        AssignmentDetails existingDetails = new AssignmentDetails(1, 100, 200, 1);

        // Act
        assignmentDetailsService.save(existingDetails);

        // Assert
        verify(assignmentDetailsDAO).save(existingDetails);
    }

    @Test
    void save_handlesCompletedAssignmentDetails() {
        // Arrange
        AssignmentDetails completedDetails = new AssignmentDetails(1, 100, 200, 1);

        // Act
        assignmentDetailsService.save(completedDetails);

        // Assert
        verify(assignmentDetailsDAO).save(completedDetails);
        assertEquals(1, completedDetails.getIsDone());
    }

    @Test
    void save_handlesIncompleteAssignmentDetails() {
        // Arrange
        AssignmentDetails incompleteDetails = new AssignmentDetails(1, 100, 200, 0);

        // Act
        assignmentDetailsService.save(incompleteDetails);

        // Assert
        verify(assignmentDetailsDAO).save(incompleteDetails);
        assertEquals(0, incompleteDetails.getIsDone());
    }

    @Test
    void deleteById_delegatesToDAO() {
        // Act
        assignmentDetailsService.deleteById(1);

        // Assert
        verify(assignmentDetailsDAO).deleteById(1);
    }

    @Test
    void deleteById_handlesNonExistentId() {
        // Act
        assignmentDetailsService.deleteById(999);

        // Assert
        verify(assignmentDetailsDAO).deleteById(999);
    }

    @Test
    void allMethods_delegateToDAO() {
        // Arrange
        AssignmentDetails details = new AssignmentDetails(1, 100, 200, 1);
        when(assignmentDetailsDAO.findByAssignmentIdAndStudentCourseDetailsId(100, 200))
                .thenReturn(details);
        when(assignmentDetailsDAO.findAll()).thenReturn(Arrays.asList(details));

        // Act & Assert
        assignmentDetailsService.findByAssignmentAndStudentCourseDetailsId(100, 200);
        verify(assignmentDetailsDAO).findByAssignmentIdAndStudentCourseDetailsId(100, 200);

        assignmentDetailsService.findAll();
        verify(assignmentDetailsDAO).findAll();

        assignmentDetailsService.save(details);
        verify(assignmentDetailsDAO).save(details);

        assignmentDetailsService.deleteById(1);
        verify(assignmentDetailsDAO).deleteById(1);
    }
}

