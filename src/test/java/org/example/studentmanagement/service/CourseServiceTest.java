package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.CourseDAO;
import org.example.studentmanagement.entity.Course;
import org.example.studentmanagement.entity.Student;
import org.example.studentmanagement.entity.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    private CourseServiceImpl courseService;
    private CourseDAO courseDAO;

    @BeforeEach
    void setUp() {
        courseDAO = mock(CourseDAO.class);
        courseService = new CourseServiceImpl(courseDAO);
    }

    @Test
    void findCourseById_returnsCourseWhenExists() {
        // Arrange
        Teacher teacher = new Teacher(1, "teacher1", "pass", "John", "Teacher", "teacher@test.com", new ArrayList<>());
        Course course = new Course(1, "MATH101", "Mathematics", teacher, new ArrayList<>());
        when(courseDAO.findById(1)).thenReturn(course);

        // Act
        Course result = courseService.findCourseById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Mathematics", result.getName());
        assertEquals("MATH101", result.getCode());
        verify(courseDAO).findById(1);
    }

    @Test
    void findCourseById_returnsNullWhenNotFound() {
        // Arrange
        when(courseDAO.findById(999)).thenReturn(null);

        // Act
        Course result = courseService.findCourseById(999);

        // Assert
        assertNull(result);
        verify(courseDAO).findById(999);
    }

    @Test
    void findCourseById_delegatesToDAO() {
        // Arrange
        Course course = new Course(5, "PHY101", "Physics", null, new ArrayList<>());
        when(courseDAO.findById(anyInt())).thenReturn(course);

        // Act
        courseService.findCourseById(5);

        // Assert
        verify(courseDAO).findById(5);
    }

    @Test
    void findAllCourses_returnsListOfCourses() {
        // Arrange
        List<Course> courses = List.of(
                new Course(1, "MATH101", "Math", null, new ArrayList<>()),
                new Course(2, "SCI101", "Science", null, new ArrayList<>()),
                new Course(3, "HIST101", "History", null, new ArrayList<>())
        );
        when(courseDAO.findAll()).thenReturn(courses);

        // Act
        List<Course> result = courseService.findAllCourses();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Math", result.get(0).getName());
        assertEquals("Science", result.get(1).getName());
        assertEquals("History", result.get(2).getName());
        verify(courseDAO).findAll();
    }

    @Test
    void findAllCourses_returnsEmptyListWhenNoCourses() {
        // Arrange
        when(courseDAO.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Course> result = courseService.findAllCourses();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(courseDAO).findAll();
    }

    @Test
    void findAllCourses_delegatesToDAO() {
        // Arrange
        when(courseDAO.findAll()).thenReturn(new ArrayList<>());

        // Act
        courseService.findAllCourses();

        // Assert
        verify(courseDAO, times(1)).findAll();
        verifyNoMoreInteractions(courseDAO);
    }

    @Test
    void save_delegatesToDAO() {
        // Arrange
        Course course = new Course(0, "NEW101", "New Course", null, new ArrayList<>());

        // Act
        courseService.save(course);

        // Assert
        verify(courseDAO).save(course);
    }

    @Test
    void save_savesCourseWithTeacher() {
        // Arrange
        Teacher teacher = new Teacher(1, "teacher", "pass", "John", "Doe", "teacher@test.com", new ArrayList<>());
        Course course = new Course(1, "MATH101", "Math", teacher, new ArrayList<>());

        // Act
        courseService.save(course);

        // Assert
        verify(courseDAO).save(course);
    }

    @Test
    void save_savesCourseWithStudents() {
        // Arrange
        List<Student> students = List.of(
                new Student(1, "student1", "pass", "John", "Doe", "s1@test.com", new ArrayList<>()),
                new Student(2, "student2", "pass", "Jane", "Smith", "s2@test.com", new ArrayList<>())
        );
        Course course = new Course(1, "MATH101", "Math", null, students);

        // Act
        courseService.save(course);

        // Assert
        verify(courseDAO).save(course);
    }

    @Test
    void deleteById_delegatesToDAO() {
        // Arrange
        int courseId = 1;

        // Act
        courseService.deleteById(courseId);

        // Assert
        verify(courseDAO).deleteById(courseId);
    }

    @Test
    void deleteById_callsDAOWithCorrectId() {
        // Arrange
        int courseId = 999;

        // Act
        courseService.deleteById(courseId);

        // Assert
        verify(courseDAO, times(1)).deleteById(999);
        verifyNoMoreInteractions(courseDAO);
    }

    @Test
    void deleteCourseById_delegatesToDAO() {
        // Arrange
        int courseId = 5;

        // Act
        courseService.deleteCourseById(courseId);

        // Assert
        verify(courseDAO).deleteById(courseId);
    }

    @Test
    void deleteCourseById_callsSameMethodAsDeleteById() {
        // Arrange
        int courseId = 10;

        // Act
        courseService.deleteCourseById(courseId);

        // Assert
        // Both methods should call the same DAO method
        verify(courseDAO).deleteById(courseId);
    }

    @Test
    void deleteCourseById_andDeleteById_areEquivalent() {
        // Arrange
        int courseId = 7;

        // Act
        courseService.deleteById(courseId);
        courseService.deleteCourseById(courseId);

        // Assert
        // Both should call deleteById on DAO twice
        verify(courseDAO, times(2)).deleteById(courseId);
    }

    @Test
    void findCourseById_returnsCourseWithAllFields() {
        // Arrange
        Teacher teacher = new Teacher(1, "teacher", "pass", "Prof", "Smith", "prof@test.com", new ArrayList<>());
        List<Student> students = List.of(
                new Student(1, "student1", "pass", "John", "Doe", "s1@test.com", new ArrayList<>())
        );
        Course course = new Course(1, "MATH201", "Advanced Math", teacher, students);
        when(courseDAO.findById(1)).thenReturn(course);

        // Act
        Course result = courseService.findCourseById(1);

        // Assert
        assertNotNull(result);
        assertEquals("Advanced Math", result.getName());
        assertEquals("MATH201", result.getCode());
        assertEquals(teacher, result.getTeacher());
        assertEquals(1, result.getStudents().size());
        verify(courseDAO).findById(1);
    }
}

