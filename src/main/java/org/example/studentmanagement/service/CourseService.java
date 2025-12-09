package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.Course;
import java.util.List;

public interface CourseService {
    Course findCourseById(int id);
    List<Course> findAllCourses();
    void save(Course course);
    void deleteById(int id);
}

