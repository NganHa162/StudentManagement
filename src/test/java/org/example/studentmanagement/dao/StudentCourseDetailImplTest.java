package org.example.studentmanagement.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import org.example.studentmanagement.entity.StudentCourseDetail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

class StudentCourseDetailImplTest {

    private EmbeddedDatabase dataSource;
    private StudentCourseDetailImpl studentCourseDetailDAO;

    @BeforeEach
    void setUp() throws Exception {
        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE student_course_detail (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "course_id INT NOT NULL, " +
                    "enrollment_date VARCHAR(255), " +
                    "status VARCHAR(50)" +
                    ")");
        }

        studentCourseDetailDAO = new StudentCourseDetailImpl(dataSource);
    }

    @AfterEach
    void tearDown() {
        if (dataSource != null) {
            dataSource.shutdown();
        }
    }

    @Test
    void save_insertsNewStudentCourseDetail() {
        StudentCourseDetail detail = new StudentCourseDetail();
        detail.setStudentId(101);
        detail.setCourseId(201);
        detail.setEnrollmentDate("2025-09-01");
        detail.setStatus("enrolled");

        studentCourseDetailDAO.save(detail);

        assertNotEquals(0, detail.getId(), "Detail ID should be set after save");
        StudentCourseDetail found = studentCourseDetailDAO.findById(detail.getId());
        assertNotNull(found, "Saved detail should be retrievable");
        assertEquals(101, found.getStudentId());
        assertEquals(201, found.getCourseId());
        assertEquals("2025-09-01", found.getEnrollmentDate());
        assertEquals("enrolled", found.getStatus());
    }

    @Test
    void save_updatesExistingStudentCourseDetail() {
        StudentCourseDetail detail = new StudentCourseDetail();
        detail.setStudentId(102);
        detail.setCourseId(202);
        detail.setEnrollmentDate("2025-08-15");
        detail.setStatus("enrolled");
        studentCourseDetailDAO.save(detail);

        detail.setStatus("completed");
        studentCourseDetailDAO.save(detail);

        StudentCourseDetail updated = studentCourseDetailDAO.findById(detail.getId());
        assertNotNull(updated);
        assertEquals("completed", updated.getStatus());
        assertEquals(102, updated.getStudentId());
        assertEquals(202, updated.getCourseId());
    }

    @Test
    void findAll_returnsAllStudentCourseDetails() {
        StudentCourseDetail detail1 = new StudentCourseDetail();
        detail1.setStudentId(101);
        detail1.setCourseId(201);
        detail1.setEnrollmentDate("2025-09-01");
        detail1.setStatus("enrolled");

        StudentCourseDetail detail2 = new StudentCourseDetail();
        detail2.setStudentId(102);
        detail2.setCourseId(202);
        detail2.setEnrollmentDate("2025-09-05");
        detail2.setStatus("enrolled");

        StudentCourseDetail detail3 = new StudentCourseDetail();
        detail3.setStudentId(103);
        detail3.setCourseId(203);
        detail3.setEnrollmentDate("2025-08-20");
        detail3.setStatus("completed");

        studentCourseDetailDAO.save(detail1);
        studentCourseDetailDAO.save(detail2);
        studentCourseDetailDAO.save(detail3);

        List<StudentCourseDetail> details = studentCourseDetailDAO.findAll();

        assertEquals(3, details.size(), "Should return all student course details");
        assertTrue(details.stream().anyMatch(d -> d.getStudentId() == 101));
        assertTrue(details.stream().anyMatch(d -> d.getStudentId() == 102));
        assertTrue(details.stream().anyMatch(d -> d.getStudentId() == 103));
    }

    @Test
    void findAll_returnsEmptyListWhenNoDetails() {
        List<StudentCourseDetail> details = studentCourseDetailDAO.findAll();

        assertNotNull(details);
        assertEquals(0, details.size(), "Should return empty list when no details exist");
    }

    @Test
    void findById_returnsStudentCourseDetailWhenExists() {
        StudentCourseDetail detail = new StudentCourseDetail();
        detail.setStudentId(104);
        detail.setCourseId(204);
        detail.setEnrollmentDate("2025-09-10");
        detail.setStatus("enrolled");
        studentCourseDetailDAO.save(detail);

        StudentCourseDetail found = studentCourseDetailDAO.findById(detail.getId());

        assertNotNull(found);
        assertEquals(detail.getId(), found.getId());
        assertEquals(104, found.getStudentId());
        assertEquals(204, found.getCourseId());
        assertEquals("2025-09-10", found.getEnrollmentDate());
        assertEquals("enrolled", found.getStatus());
    }

    @Test
    void findById_returnsNullWhenStudentCourseDetailDoesNotExist() {
        StudentCourseDetail found = studentCourseDetailDAO.findById(999);

        assertNull(found, "Should return null when detail does not exist");
    }

    @Test
    void deleteById_removesStudentCourseDetail() {
        StudentCourseDetail detail = new StudentCourseDetail();
        detail.setStudentId(105);
        detail.setCourseId(205);
        detail.setEnrollmentDate("2025-09-15");
        detail.setStatus("dropped");
        studentCourseDetailDAO.save(detail);
        int detailId = detail.getId();

        studentCourseDetailDAO.deleteById(detailId);

        StudentCourseDetail found = studentCourseDetailDAO.findById(detailId);
        assertNull(found, "Detail should be deleted");
    }

    @Test
    void deleteById_doesNotThrowExceptionWhenDetailDoesNotExist() {
        assertDoesNotThrow(() -> studentCourseDetailDAO.deleteById(999),
                "Deleting non-existent detail should not throw exception");
    }

    @Test
    void findByStudentId_returnsAllEnrollmentsForStudent() {
        StudentCourseDetail detail1 = new StudentCourseDetail();
        detail1.setStudentId(101);
        detail1.setCourseId(201);
        detail1.setEnrollmentDate("2025-09-01");
        detail1.setStatus("enrolled");

        StudentCourseDetail detail2 = new StudentCourseDetail();
        detail2.setStudentId(101);
        detail2.setCourseId(202);
        detail2.setEnrollmentDate("2025-09-01");
        detail2.setStatus("enrolled");

        StudentCourseDetail detail3 = new StudentCourseDetail();
        detail3.setStudentId(102);
        detail3.setCourseId(201);
        detail3.setEnrollmentDate("2025-09-05");
        detail3.setStatus("enrolled");

        studentCourseDetailDAO.save(detail1);
        studentCourseDetailDAO.save(detail2);
        studentCourseDetailDAO.save(detail3);

        List<StudentCourseDetail> student101Details = studentCourseDetailDAO.findByStudentId(101);

        assertEquals(2, student101Details.size(), "Should return all enrollments for student 101");
        assertTrue(student101Details.stream().allMatch(d -> d.getStudentId() == 101));
        assertTrue(student101Details.stream().anyMatch(d -> d.getCourseId() == 201));
        assertTrue(student101Details.stream().anyMatch(d -> d.getCourseId() == 202));
    }

    @Test
    void findByStudentId_returnsEmptyListWhenNoEnrollments() {
        List<StudentCourseDetail> details = studentCourseDetailDAO.findByStudentId(999);

        assertNotNull(details);
        assertEquals(0, details.size(), "Should return empty list when student has no enrollments");
    }

    @Test
    void findByCourseId_returnsAllEnrollmentsForCourse() {
        StudentCourseDetail detail1 = new StudentCourseDetail();
        detail1.setStudentId(101);
        detail1.setCourseId(201);
        detail1.setEnrollmentDate("2025-09-01");
        detail1.setStatus("enrolled");

        StudentCourseDetail detail2 = new StudentCourseDetail();
        detail2.setStudentId(102);
        detail2.setCourseId(201);
        detail2.setEnrollmentDate("2025-09-02");
        detail2.setStatus("enrolled");

        StudentCourseDetail detail3 = new StudentCourseDetail();
        detail3.setStudentId(103);
        detail3.setCourseId(202);
        detail3.setEnrollmentDate("2025-09-03");
        detail3.setStatus("enrolled");

        studentCourseDetailDAO.save(detail1);
        studentCourseDetailDAO.save(detail2);
        studentCourseDetailDAO.save(detail3);

        List<StudentCourseDetail> course201Details = studentCourseDetailDAO.findByCourseId(201);

        assertEquals(2, course201Details.size(), "Should return all enrollments for course 201");
        assertTrue(course201Details.stream().allMatch(d -> d.getCourseId() == 201));
        assertTrue(course201Details.stream().anyMatch(d -> d.getStudentId() == 101));
        assertTrue(course201Details.stream().anyMatch(d -> d.getStudentId() == 102));
    }

    @Test
    void findByCourseId_returnsEmptyListWhenNoEnrollments() {
        List<StudentCourseDetail> details = studentCourseDetailDAO.findByCourseId(999);

        assertNotNull(details);
        assertEquals(0, details.size(), "Should return empty list when course has no enrollments");
    }

    @Test
    void save_handlesMultipleStatusTypes() {
        StudentCourseDetail enrolledDetail = new StudentCourseDetail();
        enrolledDetail.setStudentId(101);
        enrolledDetail.setCourseId(201);
        enrolledDetail.setEnrollmentDate("2025-09-01");
        enrolledDetail.setStatus("enrolled");

        StudentCourseDetail completedDetail = new StudentCourseDetail();
        completedDetail.setStudentId(102);
        completedDetail.setCourseId(202);
        completedDetail.setEnrollmentDate("2025-01-15");
        completedDetail.setStatus("completed");

        StudentCourseDetail droppedDetail = new StudentCourseDetail();
        droppedDetail.setStudentId(103);
        droppedDetail.setCourseId(203);
        droppedDetail.setEnrollmentDate("2025-09-10");
        droppedDetail.setStatus("dropped");

        studentCourseDetailDAO.save(enrolledDetail);
        studentCourseDetailDAO.save(completedDetail);
        studentCourseDetailDAO.save(droppedDetail);

        List<StudentCourseDetail> allDetails = studentCourseDetailDAO.findAll();
        assertEquals(3, allDetails.size(), "Should save all different status types");
        assertTrue(allDetails.stream().anyMatch(d -> d.getStatus().equals("enrolled")));
        assertTrue(allDetails.stream().anyMatch(d -> d.getStatus().equals("completed")));
        assertTrue(allDetails.stream().anyMatch(d -> d.getStatus().equals("dropped")));
    }
}
