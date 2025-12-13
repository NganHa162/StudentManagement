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
import org.example.studentmanagement.entity.Assignment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

class AssignmentDAOImplTest {

    private EmbeddedDatabase dataSource;
    private AssignmentDAOImpl assignmentDAO;

    @BeforeEach
    void setUp() throws Exception {
        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE assignments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "course_id INT NOT NULL, " +
                    "title VARCHAR(255) NOT NULL, " +
                    "description TEXT, " +
                    "due_date VARCHAR(255), " +
                    "max_score DOUBLE, " +
                    "created_date VARCHAR(255), " +
                    "status VARCHAR(50), " +
                    "created_by_teacher_id INT" +
                    ")");
        }

        assignmentDAO = new AssignmentDAOImpl(dataSource);
    }

    @AfterEach
    void tearDown() {
        if (dataSource != null) {
            dataSource.shutdown();
        }
    }

    @Test
    void save_insertsNewAssignment() {
        Assignment assignment = new Assignment();
        assignment.setCourseId(201);
        assignment.setTitle("Homework 1");
        assignment.setDescription("Complete exercises 1-5");
        assignment.setDueDate("2025-12-15");
        assignment.setMaxScore(100.0);
        assignment.setCreatedDate("2025-12-01");
        assignment.setStatus("active");
        assignment.setCreatedByTeacherId(10);

        assignmentDAO.save(assignment);

        assertNotEquals(0, assignment.getId(), "Assignment ID should be set after save");
        Assignment found = assignmentDAO.findById(assignment.getId());
        assertNotNull(found, "Saved assignment should be retrievable");
        assertEquals(201, found.getCourseId());
        assertEquals("Homework 1", found.getTitle());
        assertEquals("Complete exercises 1-5", found.getDescription());
        assertEquals("2025-12-15", found.getDueDate());
        assertEquals(100.0, found.getMaxScore());
        assertEquals("2025-12-01", found.getCreatedDate());
        assertEquals("active", found.getStatus());
        assertEquals(10, found.getCreatedByTeacherId());
    }

    @Test
    void save_updatesExistingAssignment() {
        Assignment assignment = new Assignment();
        assignment.setCourseId(202);
        assignment.setTitle("Project Draft");
        assignment.setDescription("Submit initial draft");
        assignment.setDueDate("2025-12-10");
        assignment.setMaxScore(50.0);
        assignment.setCreatedDate("2025-11-20");
        assignment.setStatus("draft");
        assignment.setCreatedByTeacherId(11);
        assignmentDAO.save(assignment);

        assignment.setTitle("Final Project");
        assignment.setDescription("Submit final project");
        assignment.setMaxScore(100.0);
        assignment.setStatus("active");
        assignmentDAO.save(assignment);

        Assignment updated = assignmentDAO.findById(assignment.getId());
        assertNotNull(updated);
        assertEquals("Final Project", updated.getTitle());
        assertEquals("Submit final project", updated.getDescription());
        assertEquals(100.0, updated.getMaxScore());
        assertEquals("active", updated.getStatus());
    }

    @Test
    void findAll_returnsAllAssignments() {
        Assignment assignment1 = new Assignment();
        assignment1.setCourseId(201);
        assignment1.setTitle("Assignment 1");
        assignment1.setDescription("Description 1");
        assignment1.setDueDate("2025-12-05");
        assignment1.setMaxScore(100.0);
        assignment1.setCreatedDate("2025-11-25");
        assignment1.setStatus("active");
        assignment1.setCreatedByTeacherId(10);

        Assignment assignment2 = new Assignment();
        assignment2.setCourseId(202);
        assignment2.setTitle("Assignment 2");
        assignment2.setDescription("Description 2");
        assignment2.setDueDate("2025-12-10");
        assignment2.setMaxScore(100.0);
        assignment2.setCreatedDate("2025-11-28");
        assignment2.setStatus("active");
        assignment2.setCreatedByTeacherId(11);

        Assignment assignment3 = new Assignment();
        assignment3.setCourseId(203);
        assignment3.setTitle("Assignment 3");
        assignment3.setDescription("Description 3");
        assignment3.setDueDate("2025-12-20");
        assignment3.setMaxScore(150.0);
        assignment3.setCreatedDate("2025-12-01");
        assignment3.setStatus("closed");
        assignment3.setCreatedByTeacherId(12);

        assignmentDAO.save(assignment1);
        assignmentDAO.save(assignment2);
        assignmentDAO.save(assignment3);

        List<Assignment> assignments = assignmentDAO.findAll();

        assertEquals(3, assignments.size(), "Should return all assignments");
        assertTrue(assignments.stream().anyMatch(a -> a.getTitle().equals("Assignment 1")));
        assertTrue(assignments.stream().anyMatch(a -> a.getTitle().equals("Assignment 2")));
        assertTrue(assignments.stream().anyMatch(a -> a.getTitle().equals("Assignment 3")));
    }

    @Test
    void findAll_returnsEmptyListWhenNoAssignments() {
        List<Assignment> assignments = assignmentDAO.findAll();

        assertNotNull(assignments);
        assertEquals(0, assignments.size(), "Should return empty list when no assignments exist");
    }

    @Test
    void findById_returnsAssignmentWhenExists() {
        Assignment assignment = new Assignment();
        assignment.setCourseId(204);
        assignment.setTitle("Midterm Exam");
        assignment.setDescription("Comprehensive exam");
        assignment.setDueDate("2025-11-30");
        assignment.setMaxScore(200.0);
        assignment.setCreatedDate("2025-11-10");
        assignment.setStatus("active");
        assignment.setCreatedByTeacherId(13);
        assignmentDAO.save(assignment);

        Assignment found = assignmentDAO.findById(assignment.getId());

        assertNotNull(found);
        assertEquals(assignment.getId(), found.getId());
        assertEquals("Midterm Exam", found.getTitle());
        assertEquals(204, found.getCourseId());
        assertEquals(200.0, found.getMaxScore());
    }

    @Test
    void findById_returnsNullWhenAssignmentDoesNotExist() {
        Assignment found = assignmentDAO.findById(999);

        assertNull(found, "Should return null when assignment does not exist");
    }

    @Test
    void deleteById_removesAssignment() {
        Assignment assignment = new Assignment();
        assignment.setCourseId(205);
        assignment.setTitle("To Be Deleted");
        assignment.setDescription("This assignment will be deleted");
        assignment.setDueDate("2025-12-25");
        assignment.setMaxScore(100.0);
        assignment.setCreatedDate("2025-12-05");
        assignment.setStatus("draft");
        assignment.setCreatedByTeacherId(14);
        assignmentDAO.save(assignment);
        int assignmentId = assignment.getId();

        assignmentDAO.deleteById(assignmentId);

        Assignment found = assignmentDAO.findById(assignmentId);
        assertNull(found, "Assignment should be deleted");
    }

    @Test
    void deleteById_doesNotThrowExceptionWhenAssignmentDoesNotExist() {
        assertDoesNotThrow(() -> assignmentDAO.deleteById(999),
                "Deleting non-existent assignment should not throw exception");
    }

    @Test
    void findByCourseId_returnsAllAssignmentsForCourse() {
        Assignment assignment1 = new Assignment();
        assignment1.setCourseId(201);
        assignment1.setTitle("Quiz 1");
        assignment1.setDescription("Chapter 1-3");
        assignment1.setDueDate("2025-11-20");
        assignment1.setMaxScore(50.0);
        assignment1.setCreatedDate("2025-11-10");
        assignment1.setStatus("active");
        assignment1.setCreatedByTeacherId(10);

        Assignment assignment2 = new Assignment();
        assignment2.setCourseId(201);
        assignment2.setTitle("Quiz 2");
        assignment2.setDescription("Chapter 4-6");
        assignment2.setDueDate("2025-12-05");
        assignment2.setMaxScore(50.0);
        assignment2.setCreatedDate("2025-11-25");
        assignment2.setStatus("active");
        assignment2.setCreatedByTeacherId(10);

        Assignment assignment3 = new Assignment();
        assignment3.setCourseId(202);
        assignment3.setTitle("Quiz 1");
        assignment3.setDescription("Introduction");
        assignment3.setDueDate("2025-11-22");
        assignment3.setMaxScore(50.0);
        assignment3.setCreatedDate("2025-11-12");
        assignment3.setStatus("active");
        assignment3.setCreatedByTeacherId(11);

        assignmentDAO.save(assignment1);
        assignmentDAO.save(assignment2);
        assignmentDAO.save(assignment3);

        List<Assignment> course201Assignments = assignmentDAO.findByCourseId(201);

        assertEquals(2, course201Assignments.size(), "Should return all assignments for course 201");
        assertTrue(course201Assignments.stream().allMatch(a -> a.getCourseId() == 201));
        assertTrue(course201Assignments.stream().anyMatch(a -> a.getTitle().equals("Quiz 1")));
        assertTrue(course201Assignments.stream().anyMatch(a -> a.getTitle().equals("Quiz 2")));
    }

    @Test
    void findByCourseId_returnsEmptyListWhenNoAssignments() {
        List<Assignment> assignments = assignmentDAO.findByCourseId(999);

        assertNotNull(assignments);
        assertEquals(0, assignments.size(), "Should return empty list when course has no assignments");
    }

    @Test
    void findByStatus_returnsAllAssignmentsWithStatus() {
        Assignment assignment1 = new Assignment();
        assignment1.setCourseId(201);
        assignment1.setTitle("Active Assignment 1");
        assignment1.setDescription("Active");
        assignment1.setDueDate("2025-12-10");
        assignment1.setMaxScore(100.0);
        assignment1.setCreatedDate("2025-11-20");
        assignment1.setStatus("active");
        assignment1.setCreatedByTeacherId(10);

        Assignment assignment2 = new Assignment();
        assignment2.setCourseId(202);
        assignment2.setTitle("Active Assignment 2");
        assignment2.setDescription("Active");
        assignment2.setDueDate("2025-12-15");
        assignment2.setMaxScore(100.0);
        assignment2.setCreatedDate("2025-11-22");
        assignment2.setStatus("active");
        assignment2.setCreatedByTeacherId(11);

        Assignment assignment3 = new Assignment();
        assignment3.setCourseId(203);
        assignment3.setTitle("Closed Assignment");
        assignment3.setDescription("Closed");
        assignment3.setDueDate("2025-11-30");
        assignment3.setMaxScore(100.0);
        assignment3.setCreatedDate("2025-11-10");
        assignment3.setStatus("closed");
        assignment3.setCreatedByTeacherId(12);

        assignmentDAO.save(assignment1);
        assignmentDAO.save(assignment2);
        assignmentDAO.save(assignment3);

        List<Assignment> activeAssignments = assignmentDAO.findByStatus("active");

        assertEquals(2, activeAssignments.size(), "Should return all active assignments");
        assertTrue(activeAssignments.stream().allMatch(a -> a.getStatus().equals("active")));
    }

    @Test
    void findByStatus_returnsEmptyListWhenNoAssignmentsWithStatus() {
        List<Assignment> assignments = assignmentDAO.findByStatus("draft");

        assertNotNull(assignments);
        assertEquals(0, assignments.size(), "Should return empty list when no assignments have the status");
    }

    @Test
    void findByCourseIdAndStatus_returnsCorrectAssignments() {
        Assignment assignment1 = new Assignment();
        assignment1.setCourseId(201);
        assignment1.setTitle("Active for 201");
        assignment1.setDescription("Description");
        assignment1.setDueDate("2025-12-10");
        assignment1.setMaxScore(100.0);
        assignment1.setCreatedDate("2025-11-20");
        assignment1.setStatus("active");
        assignment1.setCreatedByTeacherId(10);

        Assignment assignment2 = new Assignment();
        assignment2.setCourseId(201);
        assignment2.setTitle("Closed for 201");
        assignment2.setDescription("Description");
        assignment2.setDueDate("2025-11-30");
        assignment2.setMaxScore(100.0);
        assignment2.setCreatedDate("2025-11-10");
        assignment2.setStatus("closed");
        assignment2.setCreatedByTeacherId(10);

        Assignment assignment3 = new Assignment();
        assignment3.setCourseId(202);
        assignment3.setTitle("Active for 202");
        assignment3.setDescription("Description");
        assignment3.setDueDate("2025-12-12");
        assignment3.setMaxScore(100.0);
        assignment3.setCreatedDate("2025-11-22");
        assignment3.setStatus("active");
        assignment3.setCreatedByTeacherId(11);

        assignmentDAO.save(assignment1);
        assignmentDAO.save(assignment2);
        assignmentDAO.save(assignment3);

        List<Assignment> course201ActiveAssignments = assignmentDAO.findByCourseIdAndStatus(201, "active");

        assertEquals(1, course201ActiveAssignments.size(), "Should return only active assignments for course 201");
        assertEquals("Active for 201", course201ActiveAssignments.get(0).getTitle());
        assertEquals(201, course201ActiveAssignments.get(0).getCourseId());
        assertEquals("active", course201ActiveAssignments.get(0).getStatus());
    }

    @Test
    void findByCourseIdAndStatus_returnsEmptyListWhenNoMatches() {
        List<Assignment> assignments = assignmentDAO.findByCourseIdAndStatus(999, "active");

        assertNotNull(assignments);
        assertEquals(0, assignments.size(), "Should return empty list when no matching assignments exist");
    }

    @Test
    void save_handlesMultipleStatusTypes() {
        Assignment activeAssignment = new Assignment();
        activeAssignment.setCourseId(201);
        activeAssignment.setTitle("Active");
        activeAssignment.setDescription("Active assignment");
        activeAssignment.setDueDate("2025-12-10");
        activeAssignment.setMaxScore(100.0);
        activeAssignment.setCreatedDate("2025-11-20");
        activeAssignment.setStatus("active");
        activeAssignment.setCreatedByTeacherId(10);

        Assignment closedAssignment = new Assignment();
        closedAssignment.setCourseId(202);
        closedAssignment.setTitle("Closed");
        closedAssignment.setDescription("Closed assignment");
        closedAssignment.setDueDate("2025-11-30");
        closedAssignment.setMaxScore(100.0);
        closedAssignment.setCreatedDate("2025-11-10");
        closedAssignment.setStatus("closed");
        closedAssignment.setCreatedByTeacherId(11);

        Assignment draftAssignment = new Assignment();
        draftAssignment.setCourseId(203);
        draftAssignment.setTitle("Draft");
        draftAssignment.setDescription("Draft assignment");
        draftAssignment.setDueDate("2025-12-20");
        draftAssignment.setMaxScore(100.0);
        draftAssignment.setCreatedDate("2025-12-01");
        draftAssignment.setStatus("draft");
        draftAssignment.setCreatedByTeacherId(12);

        assignmentDAO.save(activeAssignment);
        assignmentDAO.save(closedAssignment);
        assignmentDAO.save(draftAssignment);

        List<Assignment> allAssignments = assignmentDAO.findAll();
        assertEquals(3, allAssignments.size(), "Should save all different status types");
        assertTrue(allAssignments.stream().anyMatch(a -> a.getStatus().equals("active")));
        assertTrue(allAssignments.stream().anyMatch(a -> a.getStatus().equals("closed")));
        assertTrue(allAssignments.stream().anyMatch(a -> a.getStatus().equals("draft")));
    }
}
