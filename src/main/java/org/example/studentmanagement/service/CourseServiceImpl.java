package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.CourseDAO;
import org.example.studentmanagement.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO;

    @Autowired
    public CourseServiceImpl(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    @Override
    public Course findCourseById(int id) {
        return courseDAO.findById(id);
    }

    @Override
    public List<Course> findAllCourses() {
        return courseDAO.findAll();
    }

    @Override
    public void save(Course course) {
        courseDAO.save(course);
    }

    @Override
    public void deleteById(int id) {
        courseDAO.deleteById(id);
    }
}

