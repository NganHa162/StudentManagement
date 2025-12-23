package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.Course;
import org.example.studentmanagement.entity.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseDAOImplTest {

    private EmbeddedDatabase dataSource;
    private CourseDAOImpl courseDAO;

    @BeforeEach
    void setUp() throws Exception {
        // Create an in-memory H2 database
        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        // Create tables
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            // Create teachers table first (foreign key dependency)
            stmt.execute("CREATE TABLE teachers (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(255) NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "first_name VARCHAR(255), " +
                    "last_name VARCHAR(255), " +
                    "email VARCHAR(255)" +
                    ")");

            // Create courses table with foreign key to teachers
            stmt.execute("CREATE TABLE courses (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "code VARCHAR(255) NOT NULL, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "teacher_id INT, " +
                    "FOREIGN KEY (teacher_id) REFERENCES teachers(id)" +
                    ")");

            // Insert a test teacher
            stmt.execute("INSERT INTO teachers (username, password, first_name, last_name, email) " +
                    "VALUES ('prof_smith', 'pass123', 'John', 'Smith', 'smith@example.com')");
        }

        courseDAO = new CourseDAOImpl(dataSource);
    }

    @AfterEach
    void tearDown() {
        if (dataSource != null) {
            dataSource.shutdown();
        }
    }

    @Test
    void save_insertsNewCourse() {
        // Arrange
        Course course = new Course();
        course.setCode("CS101");
        course.setName("Introduction to Computer Science");

        // Act
        courseDAO.save(course);

        // Assert
        assertNotEquals(0, course.getId(), "Course ID should be set after save");
        Course found = courseDAO.findById(course.getId());
        assertNotNull(found, "Saved course should be retrievable");
        assertEquals("CS101", found.getCode());
        assertEquals("Introduction to Computer Science", found.getName());
    }

    @Test
    void save_insertsNewCourseWithTeacher() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setId(1); // Using the teacher we inserted in setUp

        Course course = new Course();
        course.setCode("CS201");
        course.setName("Data Structures");
        course.setTeacher(teacher);

        // Act
        courseDAO.save(course);

        // Assert
        assertNotEquals(0, course.getId(), "Course ID should be set after save");
        Course found = courseDAO.findById(course.getId());
        assertNotNull(found);
        assertEquals("CS201", found.getCode());
        assertEquals("Data Structures", found.getName());
    }

    @Test
    void save_updatesExistingCourse() {
        // Arrange
        Course course = new Course();
        course.setCode("MATH101");
        course.setName("Calculus I");
        courseDAO.save(course);

        // Act - Update the course
        course.setName("Calculus I - Advanced");
        course.setCode("MATH101A");
        courseDAO.save(course);

        // Assert
        Course updated = courseDAO.findById(course.getId());
        assertNotNull(updated);
        assertEquals("MATH101A", updated.getCode());
        assertEquals("Calculus I - Advanced", updated.getName());
    }

    @Test
    void findAll_returnsAllCourses() {
        // Arrange
        Course course1 = new Course();
        course1.setCode("ENG101");
        course1.setName("English Literature");

        Course course2 = new Course();
        course2.setCode("HIST101");
        course2.setName("World History");

        Course course3 = new Course();
        course3.setCode("PHYS101");
        course3.setName("Physics I");

        courseDAO.save(course1);
        courseDAO.save(course2);
        courseDAO.save(course3);

        // Act
        List<Course> courses = courseDAO.findAll();

        // Assert
        assertEquals(3, courses.size(), "Should return all courses");
        assertTrue(courses.stream().anyMatch(c -> c.getCode().equals("ENG101")));
        assertTrue(courses.stream().anyMatch(c -> c.getCode().equals("HIST101")));
        assertTrue(courses.stream().anyMatch(c -> c.getCode().equals("PHYS101")));
    }

    @Test
    void findAll_returnsEmptyListWhenNoCourses() {
        // Act
        List<Course> courses = courseDAO.findAll();

        // Assert
        assertNotNull(courses);
        assertEquals(0, courses.size(), "Should return empty list when no courses exist");
    }

    @Test
    void findById_returnsCourseWhenExists() {
        // Arrange
        Course course = new Course();
        course.setCode("TEST101");
        course.setName("Test Course");
        courseDAO.save(course);

        // Act
        Course found = courseDAO.findById(course.getId());

        // Assert
        assertNotNull(found);
        assertEquals(course.getId(), found.getId());
        assertEquals("TEST101", found.getCode());
        assertEquals("Test Course", found.getName());
    }

    @Test
    void findById_returnsNullWhenCourseDoesNotExist() {
        // Act
        Course found = courseDAO.findById(999);

        // Assert
        assertNull(found, "Should return null when course does not exist");
    }

    @Test
    void deleteById_removesCourse() {
        // Arrange
        Course course = new Course();
        course.setCode("DEL101");
        course.setName("Course to Delete");
        courseDAO.save(course);
        int courseId = course.getId();

        // Act
        courseDAO.deleteById(courseId);

        // Assert
        Course found = courseDAO.findById(courseId);
        assertNull(found, "Course should be deleted");
    }

    @Test
    void deleteById_doesNotThrowExceptionWhenCourseDoesNotExist() {
        // Act & Assert
        assertDoesNotThrow(() -> courseDAO.deleteById(999),
                "Deleting non-existent course should not throw exception");
    }

    @Test
    void save_handlesNullTeacher() {
        // Arrange
        Course course = new Course();
        course.setCode("NULL101");
        course.setName("Course Without Teacher");
        course.setTeacher(null);

        // Act & Assert
        assertDoesNotThrow(() -> courseDAO.save(course),
                "Should handle null teacher gracefully");

        Course found = courseDAO.findById(course.getId());
        assertNotNull(found);
        assertEquals("NULL101", found.getCode());
    }

    @Test
    void save_handlesMultipleCoursesWithSameName() {
        // Arrange
        Course course1 = new Course();
        course1.setCode("CS101-A");
        course1.setName("Programming");

        Course course2 = new Course();
        course2.setCode("CS101-B");
        course2.setName("Programming");

        // Act
        courseDAO.save(course1);
        courseDAO.save(course2);

        // Assert
        List<Course> courses = courseDAO.findAll();
        long programmingCount = courses.stream()
                .filter(c -> c.getName().equals("Programming"))
                .count();
        assertEquals(2, programmingCount, "Should handle multiple courses with same name");
    }
}

