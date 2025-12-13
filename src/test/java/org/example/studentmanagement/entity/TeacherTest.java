package org.example.studentmanagement.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    void addCourse_initializesListWhenNull() {
        Teacher teacher = new Teacher();
        Course course = new Course();

        teacher.addCourse(course);

        assertNotNull(teacher.getCourses(), "Courses list should be initialized after adding a course");
        assertTrue(teacher.getCourses().contains(course), "Course should be added to the teacher collection");
    }
}

