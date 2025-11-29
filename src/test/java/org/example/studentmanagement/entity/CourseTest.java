package org.example.studentmanagement.entity;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void setTeacher_linksCourseAndTeacher() {
        Course course = new Course();
        Teacher teacher = new Teacher();

        course.setTeacher(teacher);

        assertEquals(teacher, course.getTeacher(), "Course should reference the assigned teacher");
        assertTrue(teacher.getCourses().contains(course), "Teacher should automatically include the course");
    }

    @Test
    void addStudent_initializesCollection() {
        Course course = new Course();
        Student student = new Student();

        course.addStudent(student);

        assertNotNull(course.getStudents(), "Students list should be initialized after adding a student");
        assertTrue(course.getStudents().contains(student), "Added student should be present in the list");
    }

    @Test
    void removeStudent_removesWhenPresent() {
        Course course = new Course();
        Student student = new Student();
        course.setStudents(new ArrayList<>());
        course.getStudents().add(student);

        course.removeStudent(student);

        assertFalse(course.getStudents().contains(student), "Student should be removed when present");
    }

    @Test
    void equals_comparesById() {
        Course courseA = new Course();
        courseA.setId(10);
        Course courseB = new Course();
        courseB.setId(10);

        assertTrue(courseA.equals(courseB), "Courses with the same id should be equal");
    }
}

