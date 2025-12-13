package org.example.studentmanagement.entity;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void addCourse_initializesListWhenNull() {
        Student student = new Student();
        Course course = new Course();

        student.addCourse(course);

        assertNotNull(student.getCourses(), "Courses list should be initialized after adding a course");
        assertTrue(student.getCourses().contains(course), "Added course should be present in the courses list");
    }

    @Test
    void removeCourse_removesExistingCourse() {
        Student student = new Student();
        Course course = new Course();
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        student.setCourses(courses);

        student.removeCourse(course);

        assertFalse(student.getCourses().contains(course), "Course should be removed from the courses list");
    }

    @Test
    void equals_returnsTrueWhenIdsMatch() {
        Student first = new Student();
        first.setId(42);
        Student second = new Student();
        second.setId(42);

        assertTrue(first.equals(second), "Students with matching ids should be equal");
    }

    @Test
    void equals_returnsFalseWhenIdsDiffer() {
        Student first = new Student();
        first.setId(1);
        Student second = new Student();
        second.setId(2);

        assertFalse(first.equals(second), "Students with different ids should not be equal");
    }
}

