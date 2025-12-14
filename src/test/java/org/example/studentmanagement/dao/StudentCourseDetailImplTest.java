package org.example.studentmanagement.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.example.studentmanagement.entity.StudentCourseDetails;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

class StudentCourseDetailImplTest {

    private EmbeddedDatabase dataSource;
    private StudentCourseDetailsDAOImpl studentCourseDetailsDAO;

    @BeforeEach
    void setUp() throws Exception {
        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE student_course_details (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "course_id INT NOT NULL" +
                    ")");
        }

        studentCourseDetailsDAO = new StudentCourseDetailsDAOImpl(dataSource);
    }

    @AfterEach
    void tearDown() {
        if (dataSource != null) {
            dataSource.shutdown();
        }
    }

    @Test
    void save_insertsNewStudentCourseDetails() {
        StudentCourseDetails details = new StudentCourseDetails();
        details.setStudentId(101);
        details.setCourseId(201);

        studentCourseDetailsDAO.save(details);

        assertNotEquals(0, details.getId(), "Detail ID should be set after save");
        StudentCourseDetails found = studentCourseDetailsDAO.findById(details.getId());
        assertNotNull(found, "Saved details should be retrievable");
        assertEquals(101, found.getStudentId());
        assertEquals(201, found.getCourseId());
    }

    @Test
    void save_updatesExistingStudentCourseDetails() {
        StudentCourseDetails details = new StudentCourseDetails();
        details.setStudentId(102);
        details.setCourseId(202);
        studentCourseDetailsDAO.save(details);
        int originalId = details.getId();

        details.setStudentId(103);
        details.setCourseId(203);
        studentCourseDetailsDAO.save(details);

        StudentCourseDetails updated = studentCourseDetailsDAO.findById(originalId);
        assertNotNull(updated);
        assertEquals(103, updated.getStudentId());
        assertEquals(203, updated.getCourseId());
        assertEquals(originalId, updated.getId());
    }

    @Test
    void findAll_returnsAllStudentCourseDetails() {
        StudentCourseDetails details1 = new StudentCourseDetails();
        details1.setStudentId(101);
        details1.setCourseId(201);

        StudentCourseDetails details2 = new StudentCourseDetails();
        details2.setStudentId(102);
        details2.setCourseId(202);

        StudentCourseDetails details3 = new StudentCourseDetails();
        details3.setStudentId(103);
        details3.setCourseId(203);

        studentCourseDetailsDAO.save(details1);
        studentCourseDetailsDAO.save(details2);
        studentCourseDetailsDAO.save(details3);

        List<StudentCourseDetails> allDetails = studentCourseDetailsDAO.findAll();

        assertEquals(3, allDetails.size(), "Should return all student course details");
        assertTrue(allDetails.stream().anyMatch(d -> d.getStudentId() == 101));
        assertTrue(allDetails.stream().anyMatch(d -> d.getStudentId() == 102));
        assertTrue(allDetails.stream().anyMatch(d -> d.getStudentId() == 103));
    }

    @Test
    void findAll_returnsEmptyListWhenNoDetails() {
        List<StudentCourseDetails> details = studentCourseDetailsDAO.findAll();

        assertNotNull(details);
        assertEquals(0, details.size(), "Should return empty list when no details exist");
    }

    @Test
    void findById_returnsStudentCourseDetailsWhenExists() {
        StudentCourseDetails details = new StudentCourseDetails();
        details.setStudentId(104);
        details.setCourseId(204);
        studentCourseDetailsDAO.save(details);
        int detailsId = details.getId();

        StudentCourseDetails found = studentCourseDetailsDAO.findById(detailsId);

        assertNotNull(found);
        assertEquals(detailsId, found.getId());
        assertEquals(104, found.getStudentId());
        assertEquals(204, found.getCourseId());
    }

    @Test
    void findById_returnsNullWhenStudentCourseDetailsDoesNotExist() {
        StudentCourseDetails found = studentCourseDetailsDAO.findById(999);

        assertNull(found, "Should return null when details do not exist");
    }

    @Test
    void deleteById_removesStudentCourseDetails() {
        StudentCourseDetails details = new StudentCourseDetails();
        details.setStudentId(105);
        details.setCourseId(205);
        studentCourseDetailsDAO.save(details);
        int detailsId = details.getId();

        studentCourseDetailsDAO.deleteById(detailsId);

        StudentCourseDetails found = studentCourseDetailsDAO.findById(detailsId);
        assertNull(found, "Details should be deleted");
    }

    @Test
    void deleteById_doesNotThrowExceptionWhenDetailsDoNotExist() {
        assertDoesNotThrow(() -> studentCourseDetailsDAO.deleteById(999),
                "Deleting non-existent details should not throw exception");
    }

    @Test
    void findByStudentId_returnsAllEnrollmentsForStudent() {
        StudentCourseDetails details1 = new StudentCourseDetails();
        details1.setStudentId(101);
        details1.setCourseId(201);

        StudentCourseDetails details2 = new StudentCourseDetails();
        details2.setStudentId(101);
        details2.setCourseId(202);

        StudentCourseDetails details3 = new StudentCourseDetails();
        details3.setStudentId(102);
        details3.setCourseId(201);

        studentCourseDetailsDAO.save(details1);
        studentCourseDetailsDAO.save(details2);
        studentCourseDetailsDAO.save(details3);

        List<StudentCourseDetails> student101Details = studentCourseDetailsDAO.findByStudentId(101);

        assertEquals(2, student101Details.size(), "Should return all enrollments for student 101");
        assertTrue(student101Details.stream().allMatch(d -> d.getStudentId() == 101));
        assertTrue(student101Details.stream().anyMatch(d -> d.getCourseId() == 201));
        assertTrue(student101Details.stream().anyMatch(d -> d.getCourseId() == 202));
    }

    @Test
    void findByStudentId_returnsEmptyListWhenNoEnrollments() {
        List<StudentCourseDetails> details = studentCourseDetailsDAO.findByStudentId(999);

        assertNotNull(details);
        assertEquals(0, details.size(), "Should return empty list when student has no enrollments");
    }

    @Test
    void findByCourseId_returnsAllEnrollmentsForCourse() {
        StudentCourseDetails details1 = new StudentCourseDetails();
        details1.setStudentId(101);
        details1.setCourseId(201);

        StudentCourseDetails details2 = new StudentCourseDetails();
        details2.setStudentId(102);
        details2.setCourseId(201);

        StudentCourseDetails details3 = new StudentCourseDetails();
        details3.setStudentId(103);
        details3.setCourseId(202);

        studentCourseDetailsDAO.save(details1);
        studentCourseDetailsDAO.save(details2);
        studentCourseDetailsDAO.save(details3);

        List<StudentCourseDetails> course201Details = studentCourseDetailsDAO.findByCourseId(201);

        assertEquals(2, course201Details.size(), "Should return all enrollments for course 201");
        assertTrue(course201Details.stream().allMatch(d -> d.getCourseId() == 201));
        assertTrue(course201Details.stream().anyMatch(d -> d.getStudentId() == 101));
        assertTrue(course201Details.stream().anyMatch(d -> d.getStudentId() == 102));
    }

    @Test
    void findByCourseId_returnsEmptyListWhenNoEnrollments() {
        List<StudentCourseDetails> details = studentCourseDetailsDAO.findByCourseId(999);

        assertNotNull(details);
        assertEquals(0, details.size(), "Should return empty list when course has no enrollments");
    }

    @Test
    void findByStudentIdAndCourseId_returnsStudentCourseDetailsWhenExists() {
        StudentCourseDetails details1 = new StudentCourseDetails();
        details1.setStudentId(101);
        details1.setCourseId(201);

        StudentCourseDetails details2 = new StudentCourseDetails();
        details2.setStudentId(101);
        details2.setCourseId(202);

        StudentCourseDetails details3 = new StudentCourseDetails();
        details3.setStudentId(102);
        details3.setCourseId(201);

        studentCourseDetailsDAO.save(details1);
        studentCourseDetailsDAO.save(details2);
        studentCourseDetailsDAO.save(details3);

        StudentCourseDetails found = studentCourseDetailsDAO.findByStudentIdAndCourseId(101, 201);

        assertNotNull(found, "Should return details when student and course match");
        assertEquals(101, found.getStudentId());
        assertEquals(201, found.getCourseId());
    }

    @Test
    void findByStudentIdAndCourseId_returnsNullWhenDoesNotExist() {
        StudentCourseDetails details = new StudentCourseDetails();
        details.setStudentId(101);
        details.setCourseId(201);
        studentCourseDetailsDAO.save(details);

        StudentCourseDetails found = studentCourseDetailsDAO.findByStudentIdAndCourseId(999, 999);

        assertNull(found, "Should return null when combination does not exist");
    }

    @Test
    void findByStudentIdAndCourseId_returnsNullWhenOnlyStudentMatches() {
        StudentCourseDetails details = new StudentCourseDetails();
        details.setStudentId(101);
        details.setCourseId(201);
        studentCourseDetailsDAO.save(details);

        StudentCourseDetails found = studentCourseDetailsDAO.findByStudentIdAndCourseId(101, 999);

        assertNull(found, "Should return null when only student matches");
    }

    @Test
    void findByStudentIdAndCourseId_returnsNullWhenOnlyCourseMatches() {
        StudentCourseDetails details = new StudentCourseDetails();
        details.setStudentId(101);
        details.setCourseId(201);
        studentCourseDetailsDAO.save(details);

        StudentCourseDetails found = studentCourseDetailsDAO.findByStudentIdAndCourseId(999, 201);

        assertNull(found, "Should return null when only course matches");
    }

    @Test
    void save_handlesMultipleStudentCourseCombinations() {
        StudentCourseDetails details1 = new StudentCourseDetails();
        details1.setStudentId(101);
        details1.setCourseId(201);

        StudentCourseDetails details2 = new StudentCourseDetails();
        details2.setStudentId(101);
        details2.setCourseId(202);

        StudentCourseDetails details3 = new StudentCourseDetails();
        details3.setStudentId(102);
        details3.setCourseId(201);

        studentCourseDetailsDAO.save(details1);
        studentCourseDetailsDAO.save(details2);
        studentCourseDetailsDAO.save(details3);

        List<StudentCourseDetails> allDetails = studentCourseDetailsDAO.findAll();
        assertEquals(3, allDetails.size(), "Should save all different student-course combinations");
        assertTrue(allDetails.stream().anyMatch(d -> d.getStudentId() == 101 && d.getCourseId() == 201));
        assertTrue(allDetails.stream().anyMatch(d -> d.getStudentId() == 101 && d.getCourseId() == 202));
        assertTrue(allDetails.stream().anyMatch(d -> d.getStudentId() == 102 && d.getCourseId() == 201));
    }
}
