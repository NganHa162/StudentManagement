package org.example.studentmanagement.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeDetailsTest {

    @Test
    void constructor_setsAllFields() {
        GradeDetails gradeDetails = new GradeDetails(1, 101, 201, "Midterm Exam",
                                                     85.0, 100.0, "B", "Good work",
                                                     "2025-12-10", 10);

        assertEquals(1, gradeDetails.getId());
        assertEquals(101, gradeDetails.getStudentId());
        assertEquals(201, gradeDetails.getCourseId());
        assertEquals("Midterm Exam", gradeDetails.getAssignmentName());
        assertEquals(85.0, gradeDetails.getScore());
        assertEquals(100.0, gradeDetails.getMaxScore());
        assertEquals("B", gradeDetails.getGrade());
        assertEquals("Good work", gradeDetails.getFeedback());
        assertEquals("2025-12-10", gradeDetails.getGradedDate());
        assertEquals(10, gradeDetails.getGradedByTeacherId());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        GradeDetails gradeDetails = new GradeDetails();

        gradeDetails.setId(2);
        gradeDetails.setStudentId(102);
        gradeDetails.setCourseId(202);
        gradeDetails.setAssignmentName("Final Exam");
        gradeDetails.setScore(95.0);
        gradeDetails.setMaxScore(100.0);
        gradeDetails.setGrade("A");
        gradeDetails.setFeedback("Excellent");
        gradeDetails.setGradedDate("2025-12-15");
        gradeDetails.setGradedByTeacherId(20);

        assertEquals(2, gradeDetails.getId());
        assertEquals(102, gradeDetails.getStudentId());
        assertEquals(202, gradeDetails.getCourseId());
        assertEquals("Final Exam", gradeDetails.getAssignmentName());
        assertEquals(95.0, gradeDetails.getScore());
        assertEquals(100.0, gradeDetails.getMaxScore());
        assertEquals("A", gradeDetails.getGrade());
        assertEquals("Excellent", gradeDetails.getFeedback());
        assertEquals("2025-12-15", gradeDetails.getGradedDate());
        assertEquals(20, gradeDetails.getGradedByTeacherId());
    }

    @Test
    void getPercentage_calculatesCorrectly() {
        GradeDetails gradeDetails = new GradeDetails();
        gradeDetails.setScore(85.0);
        gradeDetails.setMaxScore(100.0);

        assertEquals(85.0, gradeDetails.getPercentage(), 0.01, "Percentage should be calculated correctly");
    }

    @Test
    void getPercentage_handlesPartialScore() {
        GradeDetails gradeDetails = new GradeDetails();
        gradeDetails.setScore(75.5);
        gradeDetails.setMaxScore(100.0);

        assertEquals(75.5, gradeDetails.getPercentage(), 0.01, "Percentage should handle decimal scores");
    }

    @Test
    void getPercentage_handlesNonStandardMaxScore() {
        GradeDetails gradeDetails = new GradeDetails();
        gradeDetails.setScore(45.0);
        gradeDetails.setMaxScore(50.0);

        assertEquals(90.0, gradeDetails.getPercentage(), 0.01, "Percentage should be calculated with non-standard max score");
    }

    @Test
    void getPercentage_returnsZeroWhenMaxScoreIsZero() {
        GradeDetails gradeDetails = new GradeDetails();
        gradeDetails.setScore(50.0);
        gradeDetails.setMaxScore(0.0);

        assertEquals(0.0, gradeDetails.getPercentage(), 0.01, "Percentage should return 0 when maxScore is 0");
    }

    @Test
    void getPercentage_returnsZeroWhenMaxScoreIsNegative() {
        GradeDetails gradeDetails = new GradeDetails();
        gradeDetails.setScore(50.0);
        gradeDetails.setMaxScore(-10.0);

        assertEquals(0.0, gradeDetails.getPercentage(), 0.01, "Percentage should return 0 when maxScore is negative");
    }

    @Test
    void getPercentage_handlesZeroScore() {
        GradeDetails gradeDetails = new GradeDetails();
        gradeDetails.setScore(0.0);
        gradeDetails.setMaxScore(100.0);

        assertEquals(0.0, gradeDetails.getPercentage(), 0.01, "Percentage should handle zero score");
    }

    @Test
    void getPercentage_handlesPerfectScore() {
        GradeDetails gradeDetails = new GradeDetails();
        gradeDetails.setScore(100.0);
        gradeDetails.setMaxScore(100.0);

        assertEquals(100.0, gradeDetails.getPercentage(), 0.01, "Percentage should be 100 for perfect score");
    }

    @Test
    void equals_returnsTrueWhenIdsMatch() {
        GradeDetails first = new GradeDetails();
        first.setId(1);
        GradeDetails second = new GradeDetails();
        second.setId(1);

        assertTrue(first.equals(second), "GradeDetails with matching ids should be equal");
    }

    @Test
    void equals_returnsFalseWhenIdsDiffer() {
        GradeDetails first = new GradeDetails();
        first.setId(1);
        GradeDetails second = new GradeDetails();
        second.setId(2);

        assertFalse(first.equals(second), "GradeDetails with different ids should not be equal");
    }

    @Test
    void equals_returnsTrueWhenSameObject() {
        GradeDetails gradeDetails = new GradeDetails();
        gradeDetails.setId(1);

        assertTrue(gradeDetails.equals(gradeDetails), "GradeDetails should equal itself");
    }

    @Test
    void equals_returnsFalseWhenComparedToNull() {
        GradeDetails gradeDetails = new GradeDetails();
        gradeDetails.setId(1);

        assertFalse(gradeDetails.equals(null), "GradeDetails should not equal null");
    }

    @Test
    void equals_returnsFalseWhenComparedToDifferentClass() {
        GradeDetails gradeDetails = new GradeDetails();
        gradeDetails.setId(1);
        String notGradeDetails = "Not grade details";

        assertFalse(gradeDetails.equals(notGradeDetails), "GradeDetails should not equal object of different class");
    }
}
