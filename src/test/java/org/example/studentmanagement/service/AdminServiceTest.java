package org.example.studentmanagement.service;

import java.util.Optional;

import org.example.studentmanagement.dao.AdminDAO;
import org.example.studentmanagement.dao.StudentDAO;
import org.example.studentmanagement.dao.TeacherDAO;
import org.example.studentmanagement.entity.Admin;
import org.example.studentmanagement.entity.Teacher;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

class AdminServiceTest {

    private AdminServiceImpl adminService;
    private AdminDAO adminDAO;
    private StudentDAO studentDAO;
    private TeacherDAO teacherDAO;
    private StudentCourseDetailsService studentCourseDetailsService;
    private GradeDetailsService gradeDetailsService;

    @BeforeEach
    void setUp() {
        adminDAO = mock(AdminDAO.class);
        studentDAO = mock(StudentDAO.class);
        teacherDAO = mock(TeacherDAO.class);
        studentCourseDetailsService = mock(StudentCourseDetailsService.class);
        gradeDetailsService = mock(GradeDetailsService.class);
        adminService = new AdminServiceImpl(adminDAO, studentDAO, teacherDAO, studentCourseDetailsService, gradeDetailsService);
    }

    @Test
    void findByUserName_returnsAdminWhenExists() {
        // Arrange
        Admin admin = new Admin(1000, "admin", "password123", "Admin", "System", "admin@example.com");
        when(adminDAO.findByUserName("admin")).thenReturn(Optional.of(admin));

        // Act
        Optional<Admin> result = adminService.findByUserName("admin");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1000, result.get().getId());
        assertEquals("admin", result.get().getUserName());
        verify(adminDAO).findByUserName("admin");
    }

    @Test
    void findByUserName_returnsEmptyWhenNotFound() {
        // Arrange
        when(adminDAO.findByUserName("nonexistent")).thenReturn(Optional.empty());

        // Act
        Optional<Admin> result = adminService.findByUserName("nonexistent");

        // Assert
        assertTrue(result.isEmpty());
        verify(adminDAO).findByUserName("nonexistent");
    }

    @Test
    void loadUserByUsername_returnsUserDetailsWhenAdminExists() {
        // Arrange
        Admin admin = new Admin(1000, "admin", "encoded_password", "Admin", "System", "admin@example.com");
        when(adminDAO.findByUserName("admin")).thenReturn(Optional.of(admin));

        // Act
        UserDetails userDetails = adminService.loadUserByUsername("admin");

        // Assert
        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertEquals("encoded_password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        verify(adminDAO).findByUserName("admin");
    }

    @Test
    void loadUserByUsername_throwsUsernameNotFoundExceptionWhenAdminNotFound() {
        // Arrange
        when(adminDAO.findByUserName("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> adminService.loadUserByUsername("nonexistent")
        );

        assertEquals("Admin not found: nonexistent", exception.getMessage());
        verify(adminDAO).findByUserName("nonexistent");
    }

    @Test
    void loadUserByUsername_createsUserWithCorrectRole() {
        // Arrange
        Admin admin = new Admin(2000, "testadmin", "testpass", "Test", "Admin", "test@example.com");
        when(adminDAO.findByUserName("testadmin")).thenReturn(Optional.of(admin));

        // Act
        UserDetails userDetails = adminService.loadUserByUsername("testadmin");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testadmin", userDetails.getUsername());
        assertEquals("testpass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertEquals(1, userDetails.getAuthorities().size());
    }

    @Test
    void findByUserName_delegatesToDAO() {
        // Arrange
        Admin admin = new Admin(1000, "admin", "pass", "Admin", "System", "admin@example.com");
        when(adminDAO.findByUserName(anyString())).thenReturn(Optional.of(admin));

        // Act
        adminService.findByUserName("admin");

        // Assert
        verify(adminDAO).findByUserName("admin");
    }
    
    @Test
    void createTeacher_initializesCoursesListWhenNull() {
        // Arrange
        Teacher teacher = new Teacher(0, "prof_smith", "pass123", "John", "Smith", "john@test.com", null);
        
        // Act
        adminService.createTeacher(teacher);
        
        // Assert
        assertNotNull(teacher.getCourses());
        verify(teacherDAO).save(teacher);
    }
    
    @Test
    void createTeacher_savesTeacherWithEmptyCoursesList() {
        // Arrange
        Teacher teacher = new Teacher(0, "prof_jones", "pass456", "Jane", "Jones", "jane@test.com", null);
        
        // Act
        adminService.createTeacher(teacher);
        
        // Assert
        assertNotNull(teacher.getCourses());
        assertTrue(teacher.getCourses().isEmpty());
        verify(teacherDAO).save(teacher);
    }
    
    @Test
    void updateTeacher_keepsExistingCoursesWhenNull() {
        // Arrange
        Teacher existing = new Teacher(1, "prof", "pass", "John", "Doe", "prof@test.com", new ArrayList<>());
        existing.getCourses().add(null); // Add a course
        
        Teacher updated = new Teacher(1, "prof", "pass", "Johnny", "Doe", "prof@test.com", null);
        
        when(teacherDAO.findById(1)).thenReturn(existing);
        
        // Act
        adminService.updateTeacher(updated);
        
        // Assert
        assertEquals(existing.getCourses(), updated.getCourses());
        verify(teacherDAO).save(updated);
    }
    
    @Test
    void updateTeacher_doesNotOverrideProvidedCourses() {
        // Arrange
        Teacher existing = new Teacher(1, "prof", "pass", "John", "Doe", "prof@test.com", new ArrayList<>());
        Teacher updated = new Teacher(1, "prof", "pass", "Johnny", "Doe", "prof@test.com", new ArrayList<>());
        
        when(teacherDAO.findById(1)).thenReturn(existing);
        
        // Act
        adminService.updateTeacher(updated);
        
        // Assert
        verify(teacherDAO).save(updated);
    }
}

