package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.StudentDAO;
import org.example.studentmanagement.entity.Course;
import org.example.studentmanagement.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    private StudentServiceImpl studentService;
    private StudentDAO studentDAO;

    @BeforeEach
    void setUp() {
        studentDAO = mock(StudentDAO.class);
        studentService = new StudentServiceImpl(studentDAO);
    }

    @Test
    void findByUserName_returnsStudentWhenExists() {
        // Arrange
        Student student = new Student(1, "john_doe", "password123", "John", "Doe", "john@test.com", new ArrayList<>());
        when(studentDAO.findByUserName("john_doe")).thenReturn(Optional.of(student));

        // Act
        Optional<Student> result = studentService.findByUserName("john_doe");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("john_doe", result.get().getUserName());
        assertEquals("John", result.get().getFirstName());
        verify(studentDAO).findByUserName("john_doe");
    }

    @Test
    void findByUserName_returnsEmptyWhenNotFound() {
        // Arrange
        when(studentDAO.findByUserName("nonexistent")).thenReturn(Optional.empty());

        // Act
        Optional<Student> result = studentService.findByUserName("nonexistent");

        // Assert
        assertTrue(result.isEmpty());
        verify(studentDAO).findByUserName("nonexistent");
    }

    @Test
    void loadUserByUsername_returnsUserDetailsWhenStudentExists() {
        // Arrange
        Student student = new Student(1, "john_doe", "encoded_password", "John", "Doe", "john@test.com", new ArrayList<>());
        when(studentDAO.findByUserName("john_doe")).thenReturn(Optional.of(student));

        // Act
        UserDetails userDetails = studentService.loadUserByUsername("john_doe");

        // Assert
        assertNotNull(userDetails);
        assertEquals("john_doe", userDetails.getUsername());
        assertEquals("encoded_password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT")));
        verify(studentDAO).findByUserName("john_doe");
    }

    @Test
    void loadUserByUsername_throwsUsernameNotFoundExceptionWhenStudentNotFound() {
        // Arrange
        when(studentDAO.findByUserName("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> studentService.loadUserByUsername("nonexistent")
        );

        assertEquals("Student not found: nonexistent", exception.getMessage());
        verify(studentDAO).findByUserName("nonexistent");
    }

    @Test
    void loadUserByUsername_createsUserWithCorrectRole() {
        // Arrange
        Student student = new Student(2, "jane_doe", "testpass", "Jane", "Doe", "jane@test.com", new ArrayList<>());
        when(studentDAO.findByUserName("jane_doe")).thenReturn(Optional.of(student));

        // Act
        UserDetails userDetails = studentService.loadUserByUsername("jane_doe");

        // Assert
        assertNotNull(userDetails);
        assertEquals("jane_doe", userDetails.getUsername());
        assertEquals("testpass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT")));
        assertEquals(1, userDetails.getAuthorities().size());
    }

    @Test
    void findByStudentId_returnsStudentWhenExists() {
        // Arrange
        Student student = new Student(1, "john_doe", "password", "John", "Doe", "john@test.com", new ArrayList<>());
        when(studentDAO.findById(1)).thenReturn(student);

        // Act
        Student result = studentService.findByStudentId(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("john_doe", result.getUserName());
        verify(studentDAO).findById(1);
    }

    @Test
    void findByStudentId_returnsNullWhenNotFound() {
        // Arrange
        when(studentDAO.findById(999)).thenReturn(null);

        // Act
        Student result = studentService.findByStudentId(999);

        // Assert
        assertNull(result);
        verify(studentDAO).findById(999);
    }

    @Test
    void findAllStudents_returnsListOfStudents() {
        // Arrange
        List<Student> students = List.of(
                new Student(1, "john", "pass1", "John", "Doe", "john@test.com", new ArrayList<>()),
                new Student(2, "jane", "pass2", "Jane", "Smith", "jane@test.com", new ArrayList<>())
        );
        when(studentDAO.findAll()).thenReturn(students);

        // Act
        List<Student> result = studentService.findAllStudents();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("john", result.get(0).getUserName());
        assertEquals("jane", result.get(1).getUserName());
        verify(studentDAO).findAll();
    }

    @Test
    void findAllStudents_returnsEmptyListWhenNoStudents() {
        // Arrange
        when(studentDAO.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Student> result = studentService.findAllStudents();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentDAO).findAll();
    }

    @Test
    void save_delegatesToDAO() {
        // Arrange
        Student student = new Student(0, "new_student", "password", "New", "Student", "new@test.com", new ArrayList<>());

        // Act
        studentService.save(student);

        // Assert
        verify(studentDAO).save(student);
    }

    @Test
    void save_savesStudentWithCourses() {
        // Arrange
        List<Course> courses = List.of(
                new Course(1, "Math", "MATH101", null, null),
                new Course(2, "Science", "SCI101", null, null)
        );
        Student student = new Student(1, "john", "pass", "John", "Doe", "john@test.com", courses);

        // Act
        studentService.save(student);

        // Assert
        verify(studentDAO).save(student);
    }

    @Test
    void deleteById_delegatesToDAO() {
        // Arrange
        int studentId = 1;

        // Act
        studentService.deleteById(studentId);

        // Assert
        verify(studentDAO).deleteById(studentId);
    }

    @Test
    void deleteById_callsDAOWithCorrectId() {
        // Arrange
        int studentId = 999;

        // Act
        studentService.deleteById(studentId);

        // Assert
        verify(studentDAO, times(1)).deleteById(999);
        verifyNoMoreInteractions(studentDAO);
    }

    @Test
    void findByUserName_delegatesToDAO() {
        // Arrange
        Student student = new Student(1, "test", "pass", "Test", "User", "test@test.com", new ArrayList<>());
        when(studentDAO.findByUserName(anyString())).thenReturn(Optional.of(student));

        // Act
        studentService.findByUserName("test");

        // Assert
        verify(studentDAO).findByUserName("test");
    }
}

