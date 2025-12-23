package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.StudentCourseDetailsDAO;
import org.example.studentmanagement.entity.GradeDetails;
import org.example.studentmanagement.entity.StudentCourseDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentCourseDetailsServiceTest {

    private StudentCourseDetailsServiceImpl studentCourseDetailsService;
    private StudentCourseDetailsDAO studentCourseDetailsDAO;

    @BeforeEach
    void setUp() {
        studentCourseDetailsDAO = mock(StudentCourseDetailsDAO.class);
        studentCourseDetailsService = new StudentCourseDetailsServiceImpl(studentCourseDetailsDAO);
    }

    @Test
    void findByStudentAndCourseId_returnsDetailsWhenExists() {
        // Arrange
        GradeDetails grade = new GradeDetails(1, 10, 20, "Midterm", 85.0, 100.0, "B+", "Good work", "2024-01-15", 5);
        StudentCourseDetails details = new StudentCourseDetails(1, 10, 20, grade, new ArrayList<>());
        when(studentCourseDetailsDAO.findByStudentIdAndCourseId(10, 20)).thenReturn(details);

        // Act
        StudentCourseDetails result = studentCourseDetailsService.findByStudentAndCourseId(10, 20);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(10, result.getStudentId());
        assertEquals(20, result.getCourseId());
        verify(studentCourseDetailsDAO).findByStudentIdAndCourseId(10, 20);
    }

    @Test
    void findByStudentAndCourseId_returnsNullWhenNotFound() {
        // Arrange
        when(studentCourseDetailsDAO.findByStudentIdAndCourseId(99, 99)).thenReturn(null);

        // Act
        StudentCourseDetails result = studentCourseDetailsService.findByStudentAndCourseId(99, 99);

        // Assert
        assertNull(result);
        verify(studentCourseDetailsDAO).findByStudentIdAndCourseId(99, 99);
    }

    @Test
    void findAll_returnsListOfDetails() {
        // Arrange
        List<StudentCourseDetails> detailsList = List.of(
                new StudentCourseDetails(1, 10, 20, new GradeDetails(), new ArrayList<>()),
                new StudentCourseDetails(2, 11, 21, new GradeDetails(), new ArrayList<>())
        );
        when(studentCourseDetailsDAO.findAll()).thenReturn(detailsList);

        // Act
        List<StudentCourseDetails> result = studentCourseDetailsService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(10, result.get(0).getStudentId());
        assertEquals(11, result.get(1).getStudentId());
        verify(studentCourseDetailsDAO).findAll();
    }

    @Test
    void findAll_returnsEmptyListWhenNoDetails() {
        // Arrange
        when(studentCourseDetailsDAO.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<StudentCourseDetails> result = studentCourseDetailsService.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentCourseDetailsDAO).findAll();
    }

    @Test
    void findByStudentId_returnsListOfDetailsForStudent() {
        // Arrange
        int studentId = 10;
        List<StudentCourseDetails> detailsList = List.of(
                new StudentCourseDetails(1, studentId, 20, new GradeDetails(), new ArrayList<>()),
                new StudentCourseDetails(2, studentId, 21, new GradeDetails(), new ArrayList<>())
        );
        when(studentCourseDetailsDAO.findByStudentId(studentId)).thenReturn(detailsList);

        // Act
        List<StudentCourseDetails> result = studentCourseDetailsService.findByStudentId(studentId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(20, result.get(0).getCourseId());
        assertEquals(21, result.get(1).getCourseId());
        verify(studentCourseDetailsDAO).findByStudentId(studentId);
    }

    @Test
    void findByStudentId_returnsEmptyListWhenNoEnrollments() {
        // Arrange
        when(studentCourseDetailsDAO.findByStudentId(999)).thenReturn(new ArrayList<>());

        // Act
        List<StudentCourseDetails> result = studentCourseDetailsService.findByStudentId(999);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentCourseDetailsDAO).findByStudentId(999);
    }

    @Test
    void save_delegatesToDAO() {
        // Arrange
        StudentCourseDetails details = new StudentCourseDetails(0, 10, 20, new GradeDetails(), new ArrayList<>());

        // Act
        studentCourseDetailsService.save(details);

        // Assert
        verify(studentCourseDetailsDAO).save(details);
    }

    @Test
    void save_savesDetailsWithGrade() {
        // Arrange
        GradeDetails grade = new GradeDetails(1, 10, 20, "Final Exam", 90.0, 100.0, "A-", "Excellent", "2024-01-20", 5);
        StudentCourseDetails details = new StudentCourseDetails(1, 10, 20, grade, new ArrayList<>());

        // Act
        studentCourseDetailsService.save(details);

        // Assert
        verify(studentCourseDetailsDAO).save(details);
    }

    @Test
    void deleteById_delegatesToDAO() {
        // Arrange
        int detailsId = 1;

        // Act
        studentCourseDetailsService.deleteById(detailsId);

        // Assert
        verify(studentCourseDetailsDAO).deleteById(detailsId);
    }

    @Test
    void deleteById_callsDAOWithCorrectId() {
        // Arrange
        int detailsId = 999;

        // Act
        studentCourseDetailsService.deleteById(detailsId);

        // Assert
        verify(studentCourseDetailsDAO, times(1)).deleteById(999);
        verifyNoMoreInteractions(studentCourseDetailsDAO);
    }

    @Test
    void deleteByStudentId_deletesAllEnrollmentsForStudent() {
        // Arrange
        int studentId = 10;
        List<StudentCourseDetails> detailsList = List.of(
                new StudentCourseDetails(1, studentId, 20, new GradeDetails(), new ArrayList<>()),
                new StudentCourseDetails(2, studentId, 21, new GradeDetails(), new ArrayList<>()),
                new StudentCourseDetails(3, studentId, 22, new GradeDetails(), new ArrayList<>())
        );
        when(studentCourseDetailsDAO.findByStudentId(studentId)).thenReturn(detailsList);

        // Act
        studentCourseDetailsService.deleteByStudentId(studentId);

        // Assert
        verify(studentCourseDetailsDAO).findByStudentId(studentId);
        verify(studentCourseDetailsDAO).deleteById(1);
        verify(studentCourseDetailsDAO).deleteById(2);
        verify(studentCourseDetailsDAO).deleteById(3);
        verify(studentCourseDetailsDAO, times(3)).deleteById(anyInt());
    }

    @Test
    void deleteByStudentId_doesNothingWhenNoEnrollments() {
        // Arrange
        int studentId = 999;
        when(studentCourseDetailsDAO.findByStudentId(studentId)).thenReturn(new ArrayList<>());

        // Act
        studentCourseDetailsService.deleteByStudentId(studentId);

        // Assert
        verify(studentCourseDetailsDAO).findByStudentId(studentId);
        verify(studentCourseDetailsDAO, never()).deleteById(anyInt());
    }

    @Test
    void deleteByStudentId_deletesInLoop() {
        // Arrange
        int studentId = 5;
        List<StudentCourseDetails> detailsList = List.of(
                new StudentCourseDetails(100, studentId, 1, new GradeDetails(), new ArrayList<>()),
                new StudentCourseDetails(200, studentId, 2, new GradeDetails(), new ArrayList<>())
        );
        when(studentCourseDetailsDAO.findByStudentId(studentId)).thenReturn(detailsList);

        // Act
        studentCourseDetailsService.deleteByStudentId(studentId);

        // Assert
        verify(studentCourseDetailsDAO).deleteById(100);
        verify(studentCourseDetailsDAO).deleteById(200);
    }

    @Test
    void deleteByStudentAndCourseId_deletesSpecificEnrollment() {
        // Arrange
        int studentId = 10;
        int courseId = 20;
        StudentCourseDetails details = new StudentCourseDetails(1, studentId, courseId, new GradeDetails(), new ArrayList<>());
        when(studentCourseDetailsDAO.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(details);

        // Act
        studentCourseDetailsService.deleteByStudentAndCourseId(studentId, courseId);

        // Assert
        verify(studentCourseDetailsDAO).findByStudentIdAndCourseId(studentId, courseId);
        verify(studentCourseDetailsDAO).deleteById(1);
    }

    @Test
    void deleteByStudentAndCourseId_doesNothingWhenNotFound() {
        // Arrange
        int studentId = 99;
        int courseId = 99;
        when(studentCourseDetailsDAO.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(null);

        // Act
        studentCourseDetailsService.deleteByStudentAndCourseId(studentId, courseId);

        // Assert
        verify(studentCourseDetailsDAO).findByStudentIdAndCourseId(studentId, courseId);
        verify(studentCourseDetailsDAO, never()).deleteById(anyInt());
    }

    @Test
    void deleteByStudentAndCourseId_handlesNullCheck() {
        // Arrange
        when(studentCourseDetailsDAO.findByStudentIdAndCourseId(1, 1)).thenReturn(null);

        // Act
        studentCourseDetailsService.deleteByStudentAndCourseId(1, 1);

        // Assert
        // Should not throw exception
        verify(studentCourseDetailsDAO).findByStudentIdAndCourseId(1, 1);
        verifyNoMoreInteractions(studentCourseDetailsDAO);
    }

    @Test
    void findByStudentAndCourseId_delegatesToDAO() {
        // Arrange
        StudentCourseDetails details = new StudentCourseDetails(5, 10, 20, new GradeDetails(), new ArrayList<>());
        when(studentCourseDetailsDAO.findByStudentIdAndCourseId(anyInt(), anyInt())).thenReturn(details);

        // Act
        studentCourseDetailsService.findByStudentAndCourseId(10, 20);

        // Assert
        verify(studentCourseDetailsDAO).findByStudentIdAndCourseId(10, 20);
    }

    @Test
    void findByStudentId_delegatesToDAO() {
        // Arrange
        List<StudentCourseDetails> detailsList = new ArrayList<>();
        when(studentCourseDetailsDAO.findByStudentId(anyInt())).thenReturn(detailsList);

        // Act
        studentCourseDetailsService.findByStudentId(10);

        // Assert
        verify(studentCourseDetailsDAO).findByStudentId(10);
    }
}

