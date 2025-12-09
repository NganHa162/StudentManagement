package org.example.studentmanagement.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentCourseDetailTest {

    @Test
    void constructor_setsAllFields() {
        StudentCourseDetail detail = new StudentCourseDetail(1, 101, 201, "2025-09-01", "enrolled");

        assertEquals(1, detail.getId());
        assertEquals(101, detail.getStudentId());
        assertEquals(201, detail.getCourseId());
        assertEquals("2025-09-01", detail.getEnrollmentDate());
        assertEquals("enrolled", detail.getStatus());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        StudentCourseDetail detail = new StudentCourseDetail();

        detail.setId(2);
        detail.setStudentId(102);
        detail.setCourseId(202);
        detail.setEnrollmentDate("2025-10-15");
        detail.setStatus("completed");

        assertEquals(2, detail.getId());
        assertEquals(102, detail.getStudentId());
        assertEquals(202, detail.getCourseId());
        assertEquals("2025-10-15", detail.getEnrollmentDate());
        assertEquals("completed", detail.getStatus());
    }

    @Test
    void setStatus_allowsEnrolledStatus() {
        StudentCourseDetail detail = new StudentCourseDetail();
        detail.setStatus("enrolled");

        assertEquals("enrolled", detail.getStatus(), "Status should be set to 'enrolled'");
    }

    @Test
    void setStatus_allowsCompletedStatus() {
        StudentCourseDetail detail = new StudentCourseDetail();
        detail.setStatus("completed");

        assertEquals("completed", detail.getStatus(), "Status should be set to 'completed'");
    }

    @Test
    void setStatus_allowsDroppedStatus() {
        StudentCourseDetail detail = new StudentCourseDetail();
        detail.setStatus("dropped");

        assertEquals("dropped", detail.getStatus(), "Status should be set to 'dropped'");
    }

    @Test
    void equals_returnsTrueWhenIdsMatch() {
        StudentCourseDetail first = new StudentCourseDetail();
        first.setId(1);
        StudentCourseDetail second = new StudentCourseDetail();
        second.setId(1);

        assertTrue(first.equals(second), "StudentCourseDetails with matching ids should be equal");
    }

    @Test
    void equals_returnsFalseWhenIdsDiffer() {
        StudentCourseDetail first = new StudentCourseDetail();
        first.setId(1);
        StudentCourseDetail second = new StudentCourseDetail();
        second.setId(2);

        assertFalse(first.equals(second), "StudentCourseDetails with different ids should not be equal");
    }

    @Test
    void equals_returnsTrueWhenSameObject() {
        StudentCourseDetail detail = new StudentCourseDetail();
        detail.setId(1);

        assertTrue(detail.equals(detail), "StudentCourseDetail should equal itself");
    }

    @Test
    void equals_returnsFalseWhenComparedToNull() {
        StudentCourseDetail detail = new StudentCourseDetail();
        detail.setId(1);

        assertFalse(detail.equals(null), "StudentCourseDetail should not equal null");
    }

    @Test
    void equals_returnsFalseWhenComparedToDifferentClass() {
        StudentCourseDetail detail = new StudentCourseDetail();
        detail.setId(1);
        String notADetail = "Not a detail";

        assertFalse(detail.equals(notADetail), "StudentCourseDetail should not equal object of different class");
    }

    @Test
    void defaultConstructor_createsEmptyObject() {
        StudentCourseDetail detail = new StudentCourseDetail();

        assertNotNull(detail, "Default constructor should create non-null object");
        assertEquals(0, detail.getId(), "Default id should be 0");
        assertEquals(0, detail.getStudentId(), "Default studentId should be 0");
        assertEquals(0, detail.getCourseId(), "Default courseId should be 0");
    }
}
