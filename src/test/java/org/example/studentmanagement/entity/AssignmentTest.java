package org.example.studentmanagement.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class AssignmentTest {

    @Test
    void constructor_setsAllFields() {
        Assignment assignment = new Assignment(1, 101, "Homework 1", "Complete exercises 1-5",
                                              "2025-12-15", 100.0, "2025-12-01", "active", 10);

        assertEquals(1, assignment.getId());
        assertEquals(101, assignment.getCourseId());
        assertEquals("Homework 1", assignment.getTitle());
        assertEquals("Complete exercises 1-5", assignment.getDescription());
        assertEquals("2025-12-15", assignment.getDueDate());
        assertEquals(100.0, assignment.getMaxScore());
        assertEquals("2025-12-01", assignment.getCreatedDate());
        assertEquals("active", assignment.getStatus());
        assertEquals(10, assignment.getCreatedByTeacherId());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        Assignment assignment = new Assignment();

        assignment.setId(2);
        assignment.setCourseId(102);
        assignment.setTitle("Final Project");
        assignment.setDescription("Build a web application");
        assignment.setDueDate("2025-12-20");
        assignment.setMaxScore(200.0);
        assignment.setCreatedDate("2025-12-05");
        assignment.setStatus("closed");
        assignment.setCreatedByTeacherId(20);

        assertEquals(2, assignment.getId());
        assertEquals(102, assignment.getCourseId());
        assertEquals("Final Project", assignment.getTitle());
        assertEquals("Build a web application", assignment.getDescription());
        assertEquals("2025-12-20", assignment.getDueDate());
        assertEquals(200.0, assignment.getMaxScore());
        assertEquals("2025-12-05", assignment.getCreatedDate());
        assertEquals("closed", assignment.getStatus());
        assertEquals(20, assignment.getCreatedByTeacherId());
    }

    @Test
    void isActive_returnsTrueWhenStatusIsActive() {
        Assignment assignment = new Assignment();
        assignment.setStatus("active");

        assertTrue(assignment.isActive(), "Assignment with 'active' status should return true");
    }

    @Test
    void isActive_returnsFalseWhenStatusIsNotActive() {
        Assignment assignment = new Assignment();
        assignment.setStatus("closed");

        assertFalse(assignment.isActive(), "Assignment with non-active status should return false");
    }

    @Test
    void isClosed_returnsTrueWhenStatusIsClosed() {
        Assignment assignment = new Assignment();
        assignment.setStatus("closed");

        assertTrue(assignment.isClosed(), "Assignment with 'closed' status should return true");
    }

    @Test
    void isClosed_returnsFalseWhenStatusIsNotClosed() {
        Assignment assignment = new Assignment();
        assignment.setStatus("active");

        assertFalse(assignment.isClosed(), "Assignment with non-closed status should return false");
    }

    @Test
    void equals_returnsTrueWhenIdsMatch() {
        Assignment first = new Assignment();
        first.setId(1);
        Assignment second = new Assignment();
        second.setId(1);

        assertTrue(first.equals(second), "Assignments with matching ids should be equal");
    }

    @Test
    void equals_returnsFalseWhenIdsDiffer() {
        Assignment first = new Assignment();
        first.setId(1);
        Assignment second = new Assignment();
        second.setId(2);

        assertFalse(first.equals(second), "Assignments with different ids should not be equal");
    }

    @Test
    void equals_returnsTrueWhenSameObject() {
        Assignment assignment = new Assignment();
        assignment.setId(1);

        assertTrue(assignment.equals(assignment), "Assignment should equal itself");
    }

    @Test
    void equals_returnsFalseWhenComparedToNull() {
        Assignment assignment = new Assignment();
        assignment.setId(1);

        assertFalse(assignment.equals(null), "Assignment should not equal null");
    }

    @Test
    void equals_returnsFalseWhenComparedToDifferentClass() {
        Assignment assignment = new Assignment();
        assignment.setId(1);
        String notAnAssignment = "Not an assignment";

        assertFalse(assignment.equals(notAnAssignment), "Assignment should not equal object of different class");
    }

    @Test
    void setDaysRemaining_updatesDaysRemaining() {
        Assignment assignment = new Assignment();
        assignment.setDaysRemaining(7);

        assertEquals(7, assignment.getDaysRemaining());
    }

    @Test
    void daysRemaining_canBeSetToZero() {
        Assignment assignment = new Assignment();
        assignment.setDaysRemaining(0);

        assertEquals(0, assignment.getDaysRemaining());
    }

    @Test
    void daysRemaining_canBeNegative() {
        Assignment assignment = new Assignment();
        assignment.setDaysRemaining(-5);

        assertEquals(-5, assignment.getDaysRemaining(), "Days remaining can be negative for overdue assignments");
    }
}
