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

import org.example.studentmanagement.entity.AssignmentDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

class AssignmentDetailsDAOImplTest {

    private EmbeddedDatabase dataSource;
    private AssignmentDetailsDAOImpl assignmentDetailsDAO;

    @BeforeEach
    void setUp() throws Exception {
        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE assignment_details (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "assignment_id INT NOT NULL, " +
                    "student_course_details_id INT NOT NULL, " +
                    "is_done INT NOT NULL" +
                    ")");
        }

        assignmentDetailsDAO = new AssignmentDetailsDAOImpl(dataSource);
    }

    @AfterEach
    void tearDown() {
        if (dataSource != null) {
            dataSource.shutdown();
        }
    }

    @Test
    void save_insertsNewAssignmentDetails() {
        AssignmentDetails details = new AssignmentDetails();
        details.setAssignmentId(101);
        details.setStudentCourseDetailsId(201);
        details.setIsDone(0);

        assignmentDetailsDAO.save(details);

        assertNotEquals(0, details.getId(), "ID should be set after save");
        AssignmentDetails found = assignmentDetailsDAO.findById(details.getId());
        assertNotNull(found, "Saved assignment details should be retrievable");
        assertEquals(101, found.getAssignmentId());
        assertEquals(201, found.getStudentCourseDetailsId());
        assertEquals(0, found.getIsDone());
    }

    @Test
    void save_updatesExistingAssignmentDetails() {
        AssignmentDetails details = new AssignmentDetails();
        details.setAssignmentId(102);
        details.setStudentCourseDetailsId(202);
        details.setIsDone(0);
        assignmentDetailsDAO.save(details);

        details.setIsDone(1);
        assignmentDetailsDAO.save(details);

        AssignmentDetails updated = assignmentDetailsDAO.findById(details.getId());
        assertNotNull(updated);
        assertEquals(1, updated.getIsDone(), "isDone should be updated to 1 (completed)");
        assertEquals(102, updated.getAssignmentId());
        assertEquals(202, updated.getStudentCourseDetailsId());
    }

    @Test
    void save_handlesCompletedStatus() {
        AssignmentDetails details = new AssignmentDetails();
        details.setAssignmentId(103);
        details.setStudentCourseDetailsId(203);
        details.setIsDone(1);

        assignmentDetailsDAO.save(details);

        AssignmentDetails found = assignmentDetailsDAO.findById(details.getId());
        assertNotNull(found);
        assertEquals(1, found.getIsDone(), "Should save completed status");
    }

    @Test
    void findAll_returnsAllAssignmentDetails() {
        AssignmentDetails details1 = new AssignmentDetails();
        details1.setAssignmentId(101);
        details1.setStudentCourseDetailsId(201);
        details1.setIsDone(0);

        AssignmentDetails details2 = new AssignmentDetails();
        details2.setAssignmentId(102);
        details2.setStudentCourseDetailsId(202);
        details2.setIsDone(1);

        AssignmentDetails details3 = new AssignmentDetails();
        details3.setAssignmentId(103);
        details3.setStudentCourseDetailsId(203);
        details3.setIsDone(0);

        assignmentDetailsDAO.save(details1);
        assignmentDetailsDAO.save(details2);
        assignmentDetailsDAO.save(details3);

        List<AssignmentDetails> allDetails = assignmentDetailsDAO.findAll();

        assertEquals(3, allDetails.size(), "Should return all assignment details");
        assertTrue(allDetails.stream().anyMatch(d -> d.getAssignmentId() == 101));
        assertTrue(allDetails.stream().anyMatch(d -> d.getAssignmentId() == 102));
        assertTrue(allDetails.stream().anyMatch(d -> d.getAssignmentId() == 103));
    }

    @Test
    void findAll_returnsEmptyListWhenNoDetails() {
        List<AssignmentDetails> allDetails = assignmentDetailsDAO.findAll();

        assertNotNull(allDetails);
        assertEquals(0, allDetails.size(), "Should return empty list when no assignment details exist");
    }

    @Test
    void findById_returnsDetailsWhenExists() {
        AssignmentDetails details = new AssignmentDetails();
        details.setAssignmentId(104);
        details.setStudentCourseDetailsId(204);
        details.setIsDone(1);
        assignmentDetailsDAO.save(details);

        AssignmentDetails found = assignmentDetailsDAO.findById(details.getId());

        assertNotNull(found);
        assertEquals(details.getId(), found.getId());
        assertEquals(104, found.getAssignmentId());
        assertEquals(204, found.getStudentCourseDetailsId());
        assertEquals(1, found.getIsDone());
    }

    @Test
    void findById_returnsNullWhenDetailsDoNotExist() {
        AssignmentDetails found = assignmentDetailsDAO.findById(999);

        assertNull(found, "Should return null when assignment details do not exist");
    }

    @Test
    void deleteById_removesAssignmentDetails() {
        AssignmentDetails details = new AssignmentDetails();
        details.setAssignmentId(105);
        details.setStudentCourseDetailsId(205);
        details.setIsDone(0);
        assignmentDetailsDAO.save(details);
        int detailsId = details.getId();

        assignmentDetailsDAO.deleteById(detailsId);

        AssignmentDetails found = assignmentDetailsDAO.findById(detailsId);
        assertNull(found, "Assignment details should be deleted");
    }

    @Test
    void deleteById_doesNotThrowExceptionWhenDetailsDoNotExist() {
        assertDoesNotThrow(() -> assignmentDetailsDAO.deleteById(999),
                "Deleting non-existent assignment details should not throw exception");
    }

    @Test
    void findByAssignmentIdAndStudentCourseDetailsId_returnsCorrectDetails() {
        AssignmentDetails details1 = new AssignmentDetails();
        details1.setAssignmentId(101);
        details1.setStudentCourseDetailsId(201);
        details1.setIsDone(0);

        AssignmentDetails details2 = new AssignmentDetails();
        details2.setAssignmentId(101);
        details2.setStudentCourseDetailsId(202);
        details2.setIsDone(1);

        AssignmentDetails details3 = new AssignmentDetails();
        details3.setAssignmentId(102);
        details3.setStudentCourseDetailsId(201);
        details3.setIsDone(0);

        assignmentDetailsDAO.save(details1);
        assignmentDetailsDAO.save(details2);
        assignmentDetailsDAO.save(details3);

        AssignmentDetails found = assignmentDetailsDAO.findByAssignmentIdAndStudentCourseDetailsId(101, 201);

        assertNotNull(found, "Should find assignment details by assignment ID and student course details ID");
        assertEquals(101, found.getAssignmentId());
        assertEquals(201, found.getStudentCourseDetailsId());
        assertEquals(0, found.getIsDone());
    }

    @Test
    void findByAssignmentIdAndStudentCourseDetailsId_returnsNullWhenNotFound() {
        AssignmentDetails found = assignmentDetailsDAO.findByAssignmentIdAndStudentCourseDetailsId(999, 999);

        assertNull(found, "Should return null when no matching assignment details exist");
    }

    @Test
    void findByAssignmentId_returnsAllDetailsForAssignment() {
        AssignmentDetails details1 = new AssignmentDetails();
        details1.setAssignmentId(101);
        details1.setStudentCourseDetailsId(201);
        details1.setIsDone(0);

        AssignmentDetails details2 = new AssignmentDetails();
        details2.setAssignmentId(101);
        details2.setStudentCourseDetailsId(202);
        details2.setIsDone(1);

        AssignmentDetails details3 = new AssignmentDetails();
        details3.setAssignmentId(101);
        details3.setStudentCourseDetailsId(203);
        details3.setIsDone(0);

        AssignmentDetails details4 = new AssignmentDetails();
        details4.setAssignmentId(102);
        details4.setStudentCourseDetailsId(204);
        details4.setIsDone(1);

        assignmentDetailsDAO.save(details1);
        assignmentDetailsDAO.save(details2);
        assignmentDetailsDAO.save(details3);
        assignmentDetailsDAO.save(details4);

        List<AssignmentDetails> assignment101Details = assignmentDetailsDAO.findByAssignmentId(101);

        assertEquals(3, assignment101Details.size(), "Should return all details for assignment 101");
        assertTrue(assignment101Details.stream().allMatch(d -> d.getAssignmentId() == 101));
        assertTrue(assignment101Details.stream().anyMatch(d -> d.getStudentCourseDetailsId() == 201));
        assertTrue(assignment101Details.stream().anyMatch(d -> d.getStudentCourseDetailsId() == 202));
        assertTrue(assignment101Details.stream().anyMatch(d -> d.getStudentCourseDetailsId() == 203));
    }

    @Test
    void findByAssignmentId_returnsEmptyListWhenNoDetails() {
        List<AssignmentDetails> details = assignmentDetailsDAO.findByAssignmentId(999);

        assertNotNull(details);
        assertEquals(0, details.size(), "Should return empty list when assignment has no details");
    }

    @Test
    void findByStudentCourseDetailsId_returnsAllDetailsForStudent() {
        AssignmentDetails details1 = new AssignmentDetails();
        details1.setAssignmentId(101);
        details1.setStudentCourseDetailsId(201);
        details1.setIsDone(0);

        AssignmentDetails details2 = new AssignmentDetails();
        details2.setAssignmentId(102);
        details2.setStudentCourseDetailsId(201);
        details2.setIsDone(1);

        AssignmentDetails details3 = new AssignmentDetails();
        details3.setAssignmentId(103);
        details3.setStudentCourseDetailsId(201);
        details3.setIsDone(0);

        AssignmentDetails details4 = new AssignmentDetails();
        details4.setAssignmentId(104);
        details4.setStudentCourseDetailsId(202);
        details4.setIsDone(1);

        assignmentDetailsDAO.save(details1);
        assignmentDetailsDAO.save(details2);
        assignmentDetailsDAO.save(details3);
        assignmentDetailsDAO.save(details4);

        List<AssignmentDetails> student201Details = assignmentDetailsDAO.findByStudentCourseDetailsId(201);

        assertEquals(3, student201Details.size(), "Should return all details for student course details 201");
        assertTrue(student201Details.stream().allMatch(d -> d.getStudentCourseDetailsId() == 201));
        assertTrue(student201Details.stream().anyMatch(d -> d.getAssignmentId() == 101));
        assertTrue(student201Details.stream().anyMatch(d -> d.getAssignmentId() == 102));
        assertTrue(student201Details.stream().anyMatch(d -> d.getAssignmentId() == 103));
    }

    @Test
    void findByStudentCourseDetailsId_returnsEmptyListWhenNoDetails() {
        List<AssignmentDetails> details = assignmentDetailsDAO.findByStudentCourseDetailsId(999);

        assertNotNull(details);
        assertEquals(0, details.size(), "Should return empty list when student has no assignment details");
    }

    @Test
    void save_updatesDifferentFields() {
        AssignmentDetails details = new AssignmentDetails();
        details.setAssignmentId(106);
        details.setStudentCourseDetailsId(206);
        details.setIsDone(0);
        assignmentDetailsDAO.save(details);

        details.setAssignmentId(107);
        details.setStudentCourseDetailsId(207);
        details.setIsDone(1);
        assignmentDetailsDAO.save(details);

        AssignmentDetails updated = assignmentDetailsDAO.findById(details.getId());
        assertNotNull(updated);
        assertEquals(107, updated.getAssignmentId(), "Assignment ID should be updated");
        assertEquals(207, updated.getStudentCourseDetailsId(), "Student course details ID should be updated");
        assertEquals(1, updated.getIsDone(), "isDone should be updated");
    }

    @Test
    void multipleOperations_workCorrectly() {
        AssignmentDetails details1 = new AssignmentDetails();
        details1.setAssignmentId(101);
        details1.setStudentCourseDetailsId(201);
        details1.setIsDone(0);

        AssignmentDetails details2 = new AssignmentDetails();
        details2.setAssignmentId(101);
        details2.setStudentCourseDetailsId(202);
        details2.setIsDone(0);

        assignmentDetailsDAO.save(details1);
        assignmentDetailsDAO.save(details2);

        assertEquals(2, assignmentDetailsDAO.findAll().size());
        assertEquals(2, assignmentDetailsDAO.findByAssignmentId(101).size());

        details1.setIsDone(1);
        assignmentDetailsDAO.save(details1);

        AssignmentDetails found = assignmentDetailsDAO.findByAssignmentIdAndStudentCourseDetailsId(101, 201);
        assertNotNull(found);
        assertEquals(1, found.getIsDone());

        assignmentDetailsDAO.deleteById(details2.getId());
        assertEquals(1, assignmentDetailsDAO.findByAssignmentId(101).size());

        List<AssignmentDetails> student201Details = assignmentDetailsDAO.findByStudentCourseDetailsId(201);
        assertEquals(1, student201Details.size());
        assertEquals(1, student201Details.get(0).getIsDone());
    }

    @Test
    void findByAssignmentId_includesBothCompletedAndIncomplete() {
        AssignmentDetails completed1 = new AssignmentDetails();
        completed1.setAssignmentId(101);
        completed1.setStudentCourseDetailsId(201);
        completed1.setIsDone(1);

        AssignmentDetails completed2 = new AssignmentDetails();
        completed2.setAssignmentId(101);
        completed2.setStudentCourseDetailsId(202);
        completed2.setIsDone(1);

        AssignmentDetails incomplete = new AssignmentDetails();
        incomplete.setAssignmentId(101);
        incomplete.setStudentCourseDetailsId(203);
        incomplete.setIsDone(0);

        assignmentDetailsDAO.save(completed1);
        assignmentDetailsDAO.save(completed2);
        assignmentDetailsDAO.save(incomplete);

        List<AssignmentDetails> allDetails = assignmentDetailsDAO.findByAssignmentId(101);

        assertEquals(3, allDetails.size());
        long completedCount = allDetails.stream().filter(d -> d.getIsDone() == 1).count();
        long incompleteCount = allDetails.stream().filter(d -> d.getIsDone() == 0).count();
        assertEquals(2, completedCount, "Should have 2 completed");
        assertEquals(1, incompleteCount, "Should have 1 incomplete");
    }

    @Test
    void findByStudentCourseDetailsId_includesBothCompletedAndIncomplete() {
        AssignmentDetails completed1 = new AssignmentDetails();
        completed1.setAssignmentId(101);
        completed1.setStudentCourseDetailsId(201);
        completed1.setIsDone(1);

        AssignmentDetails completed2 = new AssignmentDetails();
        completed2.setAssignmentId(102);
        completed2.setStudentCourseDetailsId(201);
        completed2.setIsDone(1);

        AssignmentDetails incomplete = new AssignmentDetails();
        incomplete.setAssignmentId(103);
        incomplete.setStudentCourseDetailsId(201);
        incomplete.setIsDone(0);

        assignmentDetailsDAO.save(completed1);
        assignmentDetailsDAO.save(completed2);
        assignmentDetailsDAO.save(incomplete);

        List<AssignmentDetails> allDetails = assignmentDetailsDAO.findByStudentCourseDetailsId(201);

        assertEquals(3, allDetails.size());
        long completedCount = allDetails.stream().filter(d -> d.getIsDone() == 1).count();
        long incompleteCount = allDetails.stream().filter(d -> d.getIsDone() == 0).count();
        assertEquals(2, completedCount, "Should have 2 completed");
        assertEquals(1, incompleteCount, "Should have 1 incomplete");
    }
}
