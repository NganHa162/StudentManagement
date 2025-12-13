package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.Student;
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

class StudentDAOImplTest {

    private EmbeddedDatabase dataSource;
    private StudentDAOImpl studentDAO;

    @BeforeEach
    void setUp() throws Exception {
        // Create an in-memory H2 database
        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        // Create students table
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE students (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(255) NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "first_name VARCHAR(255), " +
                    "last_name VARCHAR(255), " +
                    "email VARCHAR(255)" +
                    ")");
        }

        studentDAO = new StudentDAOImpl(dataSource);
    }

    @AfterEach
    void tearDown() {
        if (dataSource != null) {
            dataSource.shutdown();
        }
    }

    @Test
    void save_insertsNewStudent() {
        // Arrange
        Student student = new Student();
        student.setUserName("alice_wonder");
        student.setPassword("wonderland");
        student.setFirstName("Alice");
        student.setLastName("Wonder");
        student.setEmail("alice@example.com");

        // Act
        studentDAO.save(student);

        // Assert
        assertNotEquals(0, student.getId(), "Student ID should be set after save");
        Student found = studentDAO.findById(student.getId());
        assertNotNull(found, "Saved student should be retrievable");
        assertEquals("alice_wonder", found.getUserName());
        assertEquals("Alice", found.getFirstName());
        assertEquals("Wonder", found.getLastName());
        assertEquals("alice@example.com", found.getEmail());
    }

    @Test
    void save_updatesExistingStudent() {
        // Arrange
        Student student = new Student();
        student.setUserName("bob_builder");
        student.setPassword("build123");
        student.setFirstName("Bob");
        student.setLastName("Builder");
        student.setEmail("bob@example.com");
        studentDAO.save(student);

        // Act - Update the student
        student.setFirstName("Robert");
        student.setEmail("robert.builder@example.com");
        studentDAO.save(student);

        // Assert
        Student updated = studentDAO.findById(student.getId());
        assertNotNull(updated);
        assertEquals("Robert", updated.getFirstName());
        assertEquals("robert.builder@example.com", updated.getEmail());
        assertEquals("bob_builder", updated.getUserName()); // Unchanged
    }

    @Test
    void findAll_returnsAllStudents() {
        // Arrange
        Student student1 = new Student();
        student1.setUserName("student1");
        student1.setPassword("pass1");
        student1.setFirstName("First1");
        student1.setLastName("Last1");
        student1.setEmail("student1@example.com");

        Student student2 = new Student();
        student2.setUserName("student2");
        student2.setPassword("pass2");
        student2.setFirstName("First2");
        student2.setLastName("Last2");
        student2.setEmail("student2@example.com");

        Student student3 = new Student();
        student3.setUserName("student3");
        student3.setPassword("pass3");
        student3.setFirstName("First3");
        student3.setLastName("Last3");
        student3.setEmail("student3@example.com");

        studentDAO.save(student1);
        studentDAO.save(student2);
        studentDAO.save(student3);

        // Act
        List<Student> students = studentDAO.findAll();

        // Assert
        assertEquals(3, students.size(), "Should return all students");
        assertTrue(students.stream().anyMatch(s -> s.getUserName().equals("student1")));
        assertTrue(students.stream().anyMatch(s -> s.getUserName().equals("student2")));
        assertTrue(students.stream().anyMatch(s -> s.getUserName().equals("student3")));
    }

    @Test
    void findAll_returnsEmptyListWhenNoStudents() {
        // Act
        List<Student> students = studentDAO.findAll();

        // Assert
        assertNotNull(students);
        assertEquals(0, students.size(), "Should return empty list when no students exist");
    }

    @Test
    void findById_returnsStudentWhenExists() {
        // Arrange
        Student student = new Student();
        student.setUserName("test_student");
        student.setPassword("testpass");
        student.setFirstName("Test");
        student.setLastName("Student");
        student.setEmail("test@example.com");
        studentDAO.save(student);

        // Act
        Student found = studentDAO.findById(student.getId());

        // Assert
        assertNotNull(found);
        assertEquals(student.getId(), found.getId());
        assertEquals("test_student", found.getUserName());
        assertEquals("Test", found.getFirstName());
    }

    @Test
    void findById_returnsNullWhenStudentDoesNotExist() {
        // Act
        Student found = studentDAO.findById(999);

        // Assert
        assertNull(found, "Should return null when student does not exist");
    }

    @Test
    void deleteById_removesStudent() {
        // Arrange
        Student student = new Student();
        student.setUserName("to_delete");
        student.setPassword("pass");
        student.setFirstName("Delete");
        student.setLastName("Me");
        student.setEmail("delete@example.com");
        studentDAO.save(student);
        int studentId = student.getId();

        // Act
        studentDAO.deleteById(studentId);

        // Assert
        Student found = studentDAO.findById(studentId);
        assertNull(found, "Student should be deleted");
    }

    @Test
    void deleteById_doesNotThrowExceptionWhenStudentDoesNotExist() {
        // Act & Assert
        assertDoesNotThrow(() -> studentDAO.deleteById(999),
                "Deleting non-existent student should not throw exception");
    }

    @Test
    void save_handlesMultipleStudentsWithSameFirstName() {
        // Arrange
        Student student1 = new Student();
        student1.setUserName("john1");
        student1.setPassword("pass1");
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setEmail("john1@example.com");

        Student student2 = new Student();
        student2.setUserName("john2");
        student2.setPassword("pass2");
        student2.setFirstName("John");
        student2.setLastName("Smith");
        student2.setEmail("john2@example.com");

        // Act
        studentDAO.save(student1);
        studentDAO.save(student2);

        // Assert
        List<Student> students = studentDAO.findAll();
        assertEquals(2, students.size());
        long johnCount = students.stream()
                .filter(s -> s.getFirstName().equals("John"))
                .count();
        assertEquals(2, johnCount, "Should handle multiple students with same first name");
    }
}

