package org.example.studentmanagement.dao;

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

class TeacherDAOImplTest {

    private EmbeddedDatabase dataSource;
    private TeacherDAOImpl teacherDAO;

    @BeforeEach
    void setUp() throws Exception {
        // Create an in-memory H2 database
        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        // Create teachers table
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE teachers (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(255) NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "first_name VARCHAR(255), " +
                    "last_name VARCHAR(255), " +
                    "email VARCHAR(255)" +
                    ")");
        }

        teacherDAO = new TeacherDAOImpl(dataSource);
    }

    @AfterEach
    void tearDown() {
        if (dataSource != null) {
            dataSource.shutdown();
        }
    }

    @Test
    void save_insertsNewTeacher() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setUserName("john_doe");
        teacher.setPassword("password123");
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setEmail("john.doe@example.com");

        // Act
        teacherDAO.save(teacher);

        // Assert
        assertNotEquals(0, teacher.getId(), "Teacher ID should be set after save");
        Teacher found = teacherDAO.findById(teacher.getId());
        assertNotNull(found, "Saved teacher should be retrievable");
        assertEquals("john_doe", found.getUserName());
        assertEquals("John", found.getFirstName());
        assertEquals("Doe", found.getLastName());
        assertEquals("john.doe@example.com", found.getEmail());
    }

    @Test
    void save_updatesExistingTeacher() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setUserName("jane_smith");
        teacher.setPassword("password456");
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");
        teacher.setEmail("jane.smith@example.com");
        teacherDAO.save(teacher);

        // Act - Update the teacher
        teacher.setFirstName("Janet");
        teacher.setEmail("janet.smith@example.com");
        teacherDAO.save(teacher);

        // Assert
        Teacher updated = teacherDAO.findById(teacher.getId());
        assertNotNull(updated);
        assertEquals("Janet", updated.getFirstName());
        assertEquals("janet.smith@example.com", updated.getEmail());
        assertEquals("jane_smith", updated.getUserName()); // Unchanged
    }

    @Test
    void findAll_returnsAllTeachers() {
        // Arrange
        Teacher teacher1 = new Teacher();
        teacher1.setUserName("teacher1");
        teacher1.setPassword("pass1");
        teacher1.setFirstName("First1");
        teacher1.setLastName("Last1");
        teacher1.setEmail("teacher1@example.com");

        Teacher teacher2 = new Teacher();
        teacher2.setUserName("teacher2");
        teacher2.setPassword("pass2");
        teacher2.setFirstName("First2");
        teacher2.setLastName("Last2");
        teacher2.setEmail("teacher2@example.com");

        teacherDAO.save(teacher1);
        teacherDAO.save(teacher2);

        // Act
        List<Teacher> teachers = teacherDAO.findAll();

        // Assert
        assertEquals(2, teachers.size(), "Should return all teachers");
        assertTrue(teachers.stream().anyMatch(t -> t.getUserName().equals("teacher1")));
        assertTrue(teachers.stream().anyMatch(t -> t.getUserName().equals("teacher2")));
    }

    @Test
    void findAll_returnsEmptyListWhenNoTeachers() {
        // Act
        List<Teacher> teachers = teacherDAO.findAll();

        // Assert
        assertNotNull(teachers);
        assertEquals(0, teachers.size(), "Should return empty list when no teachers exist");
    }

    @Test
    void findById_returnsTeacherWhenExists() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setUserName("test_teacher");
        teacher.setPassword("testpass");
        teacher.setFirstName("Test");
        teacher.setLastName("Teacher");
        teacher.setEmail("test@example.com");
        teacherDAO.save(teacher);

        // Act
        Teacher found = teacherDAO.findById(teacher.getId());

        // Assert
        assertNotNull(found);
        assertEquals(teacher.getId(), found.getId());
        assertEquals("test_teacher", found.getUserName());
        assertEquals("Test", found.getFirstName());
    }

    @Test
    void findById_returnsNullWhenTeacherDoesNotExist() {
        // Act
        Teacher found = teacherDAO.findById(999);

        // Assert
        assertNull(found, "Should return null when teacher does not exist");
    }

    @Test
    void deleteById_removesTeacher() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setUserName("to_delete");
        teacher.setPassword("pass");
        teacher.setFirstName("Delete");
        teacher.setLastName("Me");
        teacher.setEmail("delete@example.com");
        teacherDAO.save(teacher);
        int teacherId = teacher.getId();

        // Act
        teacherDAO.deleteById(teacherId);

        // Assert
        Teacher found = teacherDAO.findById(teacherId);
        assertNull(found, "Teacher should be deleted");
    }

    @Test
    void deleteById_doesNotThrowExceptionWhenTeacherDoesNotExist() {
        // Act & Assert
        assertDoesNotThrow(() -> teacherDAO.deleteById(999),
                "Deleting non-existent teacher should not throw exception");
    }
}

