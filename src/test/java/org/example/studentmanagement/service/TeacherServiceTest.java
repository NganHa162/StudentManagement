package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.TeacherDAO;
import org.example.studentmanagement.entity.Course;
import org.example.studentmanagement.entity.Teacher;
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

class TeacherServiceTest {

    private TeacherServiceImpl teacherService;
    private TeacherDAO teacherDAO;

    @BeforeEach
    void setUp() {
        teacherDAO = mock(TeacherDAO.class);
        teacherService = new TeacherServiceImpl(teacherDAO);
    }

    @Test
    void findByUserName_returnsTeacherWhenExists() {
        // Arrange
        Teacher teacher = new Teacher(1, "prof_smith", "password123", "John", "Smith", "john@test.com", new ArrayList<>());
        when(teacherDAO.findByUserName("prof_smith")).thenReturn(Optional.of(teacher));

        // Act
        Optional<Teacher> result = teacherService.findByUserName("prof_smith");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("prof_smith", result.get().getUserName());
        assertEquals("John", result.get().getFirstName());
        verify(teacherDAO).findByUserName("prof_smith");
    }

    @Test
    void findByUserName_returnsEmptyWhenNotFound() {
        // Arrange
        when(teacherDAO.findByUserName("nonexistent")).thenReturn(Optional.empty());

        // Act
        Optional<Teacher> result = teacherService.findByUserName("nonexistent");

        // Assert
        assertTrue(result.isEmpty());
        verify(teacherDAO).findByUserName("nonexistent");
    }

    @Test
    void loadUserByUsername_returnsUserDetailsWhenTeacherExists() {
        // Arrange
        Teacher teacher = new Teacher(1, "prof_smith", "encoded_password", "John", "Smith", "john@test.com", new ArrayList<>());
        when(teacherDAO.findByUserName("prof_smith")).thenReturn(Optional.of(teacher));

        // Act
        UserDetails userDetails = teacherService.loadUserByUsername("prof_smith");

        // Assert
        assertNotNull(userDetails);
        assertEquals("prof_smith", userDetails.getUsername());
        assertEquals("encoded_password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER")));
        verify(teacherDAO).findByUserName("prof_smith");
    }

    @Test
    void loadUserByUsername_throwsUsernameNotFoundExceptionWhenTeacherNotFound() {
        // Arrange
        when(teacherDAO.findByUserName("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> teacherService.loadUserByUsername("nonexistent")
        );

        assertEquals("Teacher not found: nonexistent", exception.getMessage());
        verify(teacherDAO).findByUserName("nonexistent");
    }

    @Test
    void loadUserByUsername_createsUserWithCorrectRole() {
        // Arrange
        Teacher teacher = new Teacher(2, "prof_jones", "testpass", "Jane", "Jones", "jane@test.com", new ArrayList<>());
        when(teacherDAO.findByUserName("prof_jones")).thenReturn(Optional.of(teacher));

        // Act
        UserDetails userDetails = teacherService.loadUserByUsername("prof_jones");

        // Assert
        assertNotNull(userDetails);
        assertEquals("prof_jones", userDetails.getUsername());
        assertEquals("testpass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER")));
        assertEquals(1, userDetails.getAuthorities().size());
    }

    @Test
    void findByTeacherId_returnsTeacherWhenExists() {
        // Arrange
        Teacher teacher = new Teacher(1, "prof_smith", "password", "John", "Smith", "john@test.com", new ArrayList<>());
        when(teacherDAO.findById(1)).thenReturn(teacher);

        // Act
        Teacher result = teacherService.findByTeacherId(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("prof_smith", result.getUserName());
        verify(teacherDAO).findById(1);
    }

    @Test
    void findByTeacherId_returnsNullWhenNotFound() {
        // Arrange
        when(teacherDAO.findById(999)).thenReturn(null);

        // Act
        Teacher result = teacherService.findByTeacherId(999);

        // Assert
        assertNull(result);
        verify(teacherDAO).findById(999);
    }

    @Test
    void findAllTeachers_returnsListOfTeachers() {
        // Arrange
        List<Teacher> teachers = List.of(
                new Teacher(1, "prof1", "pass1", "John", "Smith", "john@test.com", new ArrayList<>()),
                new Teacher(2, "prof2", "pass2", "Jane", "Jones", "jane@test.com", new ArrayList<>())
        );
        when(teacherDAO.findAll()).thenReturn(teachers);

        // Act
        List<Teacher> result = teacherService.findAllTeachers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("prof1", result.get(0).getUserName());
        assertEquals("prof2", result.get(1).getUserName());
        verify(teacherDAO).findAll();
    }

    @Test
    void findAllTeachers_returnsEmptyListWhenNoTeachers() {
        // Arrange
        when(teacherDAO.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Teacher> result = teacherService.findAllTeachers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(teacherDAO).findAll();
    }

    @Test
    void save_delegatesToDAO() {
        // Arrange
        Teacher teacher = new Teacher(0, "new_prof", "password", "New", "Teacher", "new@test.com", new ArrayList<>());

        // Act
        teacherService.save(teacher);

        // Assert
        verify(teacherDAO).save(teacher);
    }

    @Test
    void save_savesTeacherWithCourses() {
        // Arrange
        List<Course> courses = List.of(
                new Course(1, "MATH101", "Math", null, null),
                new Course(2, "SCI101", "Science", null, null)
        );
        Teacher teacher = new Teacher(1, "prof", "pass", "John", "Doe", "prof@test.com", courses);

        // Act
        teacherService.save(teacher);

        // Assert
        verify(teacherDAO).save(teacher);
    }

    @Test
    void deleteById_delegatesToDAO() {
        // Arrange
        int teacherId = 1;

        // Act
        teacherService.deleteById(teacherId);

        // Assert
        verify(teacherDAO).deleteById(teacherId);
    }

    @Test
    void deleteById_callsDAOWithCorrectId() {
        // Arrange
        int teacherId = 999;

        // Act
        teacherService.deleteById(teacherId);

        // Assert
        verify(teacherDAO, times(1)).deleteById(999);
        verifyNoMoreInteractions(teacherDAO);
    }

    @Test
    void deleteTeacherById_delegatesToDAO() {
        // Arrange
        int teacherId = 5;

        // Act
        teacherService.deleteTeacherById(teacherId);

        // Assert
        verify(teacherDAO).deleteById(teacherId);
    }

    @Test
    void deleteTeacherById_callsSameMethodAsDeleteById() {
        // Arrange
        int teacherId = 10;

        // Act
        teacherService.deleteTeacherById(teacherId);

        // Assert
        // Both methods should call the same DAO method
        verify(teacherDAO).deleteById(teacherId);
    }

    @Test
    void deleteTeacherById_andDeleteById_areEquivalent() {
        // Arrange
        int teacherId = 7;

        // Act
        teacherService.deleteById(teacherId);
        teacherService.deleteTeacherById(teacherId);

        // Assert
        // Both should call deleteById on DAO twice
        verify(teacherDAO, times(2)).deleteById(teacherId);
    }

    @Test
    void findByUserName_delegatesToDAO() {
        // Arrange
        Teacher teacher = new Teacher(1, "test", "pass", "Test", "User", "test@test.com", new ArrayList<>());
        when(teacherDAO.findByUserName(anyString())).thenReturn(Optional.of(teacher));

        // Act
        teacherService.findByUserName("test");

        // Assert
        verify(teacherDAO).findByUserName("test");
    }

    @Test
    void findByTeacherId_returnTeacherWithAllFields() {
        // Arrange
        List<Course> courses = List.of(
                new Course(1, "MATH101", "Mathematics", null, new ArrayList<>())
        );
        Teacher teacher = new Teacher(1, "prof", "pass", "Prof", "Smith", "prof@test.com", courses);
        when(teacherDAO.findById(1)).thenReturn(teacher);

        // Act
        Teacher result = teacherService.findByTeacherId(1);

        // Assert
        assertNotNull(result);
        assertEquals("prof", result.getUserName());
        assertEquals("Prof", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals(1, result.getCourses().size());
        verify(teacherDAO).findById(1);
    }
}

