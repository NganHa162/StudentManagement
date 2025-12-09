package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.StudentCourseDetailsDAO;
import org.example.studentmanagement.entity.StudentCourseDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentCourseDetailsServiceImpl implements StudentCourseDetailsService {

    private final StudentCourseDetailsDAO studentCourseDetailsDAO;

    @Autowired
    public StudentCourseDetailsServiceImpl(StudentCourseDetailsDAO studentCourseDetailsDAO) {
        this.studentCourseDetailsDAO = studentCourseDetailsDAO;
    }

    @Override
    public StudentCourseDetails findByStudentAndCourseId(int studentId, int courseId) {
        return studentCourseDetailsDAO.findByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public List<StudentCourseDetails> findAll() {
        return studentCourseDetailsDAO.findAll();
    }

    @Override
    public void save(StudentCourseDetails studentCourseDetails) {
        studentCourseDetailsDAO.save(studentCourseDetails);
    }

    @Override
    public void deleteById(int id) {
        studentCourseDetailsDAO.deleteById(id);
    }
}

