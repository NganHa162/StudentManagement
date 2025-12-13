package org.example.studentmanagement.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class AssignmentDetailsTest {

    @Test
    void constructor_setsAllFields() {
        AssignmentDetails assignmentDetails = new AssignmentDetails(1, 101, 201, 1);

        assertEquals(1, assignmentDetails.getId());
        assertEquals(101, assignmentDetails.getAssignmentId());
        assertEquals(201, assignmentDetails.getStudentCourseDetailsId());
        assertEquals(1, assignmentDetails.getIsDone());
    }

    @Test
    void defaultConstructor_createsEmptyObject() {
        AssignmentDetails assignmentDetails = new AssignmentDetails();

        assertEquals(0, assignmentDetails.getId());
        assertEquals(0, assignmentDetails.getAssignmentId());
        assertEquals(0, assignmentDetails.getStudentCourseDetailsId());
        assertEquals(0, assignmentDetails.getIsDone());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        AssignmentDetails assignmentDetails = new AssignmentDetails();

        assignmentDetails.setId(2);
        assignmentDetails.setAssignmentId(102);
        assignmentDetails.setStudentCourseDetailsId(202);
        assignmentDetails.setIsDone(0);

        assertEquals(2, assignmentDetails.getId());
        assertEquals(102, assignmentDetails.getAssignmentId());
        assertEquals(202, assignmentDetails.getStudentCourseDetailsId());
        assertEquals(0, assignmentDetails.getIsDone());
    }

    @Test
    void isDone_canBeSetToZero() {
        AssignmentDetails assignmentDetails = new AssignmentDetails();
        assignmentDetails.setIsDone(0);

        assertEquals(0, assignmentDetails.getIsDone(), "isDone should be 0 for incomplete assignments");
    }

    @Test
    void isDone_canBeSetToOne() {
        AssignmentDetails assignmentDetails = new AssignmentDetails();
        assignmentDetails.setIsDone(1);

        assertEquals(1, assignmentDetails.getIsDone(), "isDone should be 1 for completed assignments");
    }

    @Test
    void equals_returnsTrueWhenIdsMatch() {
        AssignmentDetails first = new AssignmentDetails();
        first.setId(1);
        AssignmentDetails second = new AssignmentDetails();
        second.setId(1);

        assertTrue(first.equals(second), "AssignmentDetails with matching ids should be equal");
    }

    @Test
    void equals_returnsFalseWhenIdsDiffer() {
        AssignmentDetails first = new AssignmentDetails();
        first.setId(1);
        AssignmentDetails second = new AssignmentDetails();
        second.setId(2);

        assertFalse(first.equals(second), "AssignmentDetails with different ids should not be equal");
    }

    @Test
    void equals_returnsTrueWhenSameObject() {
        AssignmentDetails assignmentDetails = new AssignmentDetails();
        assignmentDetails.setId(1);

        assertTrue(assignmentDetails.equals(assignmentDetails), "AssignmentDetails should equal itself");
    }

    @Test
    void equals_returnsFalseWhenComparedToNull() {
        AssignmentDetails assignmentDetails = new AssignmentDetails();
        assignmentDetails.setId(1);

        assertFalse(assignmentDetails.equals(null), "AssignmentDetails should not equal null");
    }

    @Test
    void equals_returnsFalseWhenComparedToDifferentClass() {
        AssignmentDetails assignmentDetails = new AssignmentDetails();
        assignmentDetails.setId(1);
        String notAnAssignmentDetails = "Not an AssignmentDetails";

        assertFalse(assignmentDetails.equals(notAnAssignmentDetails), 
                   "AssignmentDetails should not equal object of different class");
    }

    @Test
    void equals_ignoresOtherFieldsWhenComparing() {
        AssignmentDetails first = new AssignmentDetails(1, 101, 201, 1);
        AssignmentDetails second = new AssignmentDetails(1, 999, 888, 0);

        assertTrue(first.equals(second), 
                  "AssignmentDetails should be equal based on id only, ignoring other fields");
    }
}

