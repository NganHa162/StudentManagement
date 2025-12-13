package org.example.studentmanagement.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class StudentCourseDetailTest {

    @Test
    void constructor_setsAllFields() {
        GradeDetails gradeDetails = new GradeDetails();
        gradeDetails.setId(1);
        List<Assignment> assignments = new ArrayList<>();
        Assignment assignment = new Assignment();
        assignment.setId(101);
        assignments.add(assignment);

        StudentCourseDetails details = new StudentCourseDetails(1, 101, 201, gradeDetails, assignments);

        assertEquals(1, details.getId());
        assertEquals(101, details.getStudentId());
        assertEquals(201, details.getCourseId());
        assertEquals(gradeDetails, details.getGradeDetails());
        assertEquals(assignments, details.getAssignments());
    }

    @Test
    void defaultConstructor_createsEmptyObject() {
        StudentCourseDetails details = new StudentCourseDetails();

        assertEquals(0, details.getId());
        assertEquals(0, details.getStudentId());
        assertEquals(0, details.getCourseId());
        assertNull(details.getGradeDetails());
        assertNull(details.getAssignments());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        StudentCourseDetails details = new StudentCourseDetails();
        GradeDetails gradeDetails = new GradeDetails();
        gradeDetails.setId(1);
        List<Assignment> assignments = new ArrayList<>();
        Assignment assignment1 = new Assignment();
        assignment1.setId(101);
        assignments.add(assignment1);

        details.setId(2);
        details.setStudentId(102);
        details.setCourseId(202);
        details.setGradeDetails(gradeDetails);
        details.setAssignments(assignments);

        assertEquals(2, details.getId());
        assertEquals(102, details.getStudentId());
        assertEquals(202, details.getCourseId());
        assertEquals(gradeDetails, details.getGradeDetails());
        assertEquals(assignments, details.getAssignments());
    }

    @Test
    void getAssignmentById_returnsAssignmentWhenExists() {
        StudentCourseDetails details = new StudentCourseDetails();
        List<Assignment> assignments = new ArrayList<>();
        Assignment assignment1 = new Assignment();
        assignment1.setId(101);
        assignment1.setTitle("Assignment 1");
        Assignment assignment2 = new Assignment();
        assignment2.setId(102);
        assignment2.setTitle("Assignment 2");
        assignments.add(assignment1);
        assignments.add(assignment2);
        details.setAssignments(assignments);

        Assignment found = details.getAssignmentById(101);

        assertNotNull(found, "Should return assignment when it exists");
        assertEquals(101, found.getId());
        assertEquals("Assignment 1", found.getTitle());
    }

    @Test
    void getAssignmentById_returnsNullWhenAssignmentDoesNotExist() {
        StudentCourseDetails details = new StudentCourseDetails();
        List<Assignment> assignments = new ArrayList<>();
        Assignment assignment1 = new Assignment();
        assignment1.setId(101);
        assignments.add(assignment1);
        details.setAssignments(assignments);

        Assignment found = details.getAssignmentById(999);

        assertNull(found, "Should return null when assignment does not exist");
    }

    @Test
    void getAssignmentById_returnsNullWhenAssignmentsListIsNull() {
        StudentCourseDetails details = new StudentCourseDetails();
        details.setAssignments(null);

        Assignment found = details.getAssignmentById(101);

        assertNull(found, "Should return null when assignments list is null");
    }

    @Test
    void getAssignmentById_returnsNullWhenAssignmentsListIsEmpty() {
        StudentCourseDetails details = new StudentCourseDetails();
        details.setAssignments(new ArrayList<>());

        Assignment found = details.getAssignmentById(101);

        assertNull(found, "Should return null when assignments list is empty");
    }

    @Test
    void equals_returnsTrueWhenIdsMatch() {
        StudentCourseDetails first = new StudentCourseDetails();
        first.setId(1);
        StudentCourseDetails second = new StudentCourseDetails();
        second.setId(1);

        assertTrue(first.equals(second), "StudentCourseDetails with matching ids should be equal");
    }

    @Test
    void equals_returnsFalseWhenIdsDiffer() {
        StudentCourseDetails first = new StudentCourseDetails();
        first.setId(1);
        StudentCourseDetails second = new StudentCourseDetails();
        second.setId(2);

        assertFalse(first.equals(second), "StudentCourseDetails with different ids should not be equal");
    }

    @Test
    void equals_returnsTrueWhenSameObject() {
        StudentCourseDetails details = new StudentCourseDetails();
        details.setId(1);

        assertTrue(details.equals(details), "StudentCourseDetails should equal itself");
    }

    @Test
    void equals_returnsFalseWhenComparedToNull() {
        StudentCourseDetails details = new StudentCourseDetails();
        details.setId(1);

        assertFalse(details.equals(null), "StudentCourseDetails should not equal null");
    }

    @Test
    void equals_returnsFalseWhenComparedToDifferentClass() {
        StudentCourseDetails details = new StudentCourseDetails();
        details.setId(1);
        String notADetail = "Not a StudentCourseDetails";

        assertFalse(details.equals(notADetail), "StudentCourseDetails should not equal object of different class");
    }

    @Test
    void equals_ignoresOtherFieldsWhenComparing() {
        GradeDetails gradeDetails1 = new GradeDetails();
        gradeDetails1.setId(1);
        List<Assignment> assignments1 = new ArrayList<>();
        StudentCourseDetails first = new StudentCourseDetails(1, 101, 201, gradeDetails1, assignments1);

        GradeDetails gradeDetails2 = new GradeDetails();
        gradeDetails2.setId(2);
        List<Assignment> assignments2 = new ArrayList<>();
        StudentCourseDetails second = new StudentCourseDetails(1, 999, 888, gradeDetails2, assignments2);

        assertTrue(first.equals(second), 
                  "StudentCourseDetails should be equal based on id only, ignoring other fields");
    }

    @Test
    void gradeDetails_canBeSetToNull() {
        StudentCourseDetails details = new StudentCourseDetails();
        details.setGradeDetails(null);

        assertNull(details.getGradeDetails(), "GradeDetails should be able to be set to null");
    }

    @Test
    void assignments_canBeSetToNull() {
        StudentCourseDetails details = new StudentCourseDetails();
        details.setAssignments(null);

        assertNull(details.getAssignments(), "Assignments should be able to be set to null");
    }

    @Test
    void assignments_canBeSetToEmptyList() {
        StudentCourseDetails details = new StudentCourseDetails();
        details.setAssignments(new ArrayList<>());

        assertNotNull(details.getAssignments(), "Assignments should be able to be set to empty list");
        assertTrue(details.getAssignments().isEmpty(), "Assignments list should be empty");
    }
}
