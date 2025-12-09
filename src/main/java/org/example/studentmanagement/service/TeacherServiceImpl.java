package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.TeacherDAO;
import org.example.studentmanagement.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherDAO teacherDAO;

    @Autowired
    public TeacherServiceImpl(TeacherDAO teacherDAO) {
        this.teacherDAO = teacherDAO;
    }

    @Override
    public Teacher findByTeacherId(int id) {
        return teacherDAO.findById(id);
    }

    @Override
    public List<Teacher> findAllTeachers() {
        return teacherDAO.findAll();
    }

    @Override
    public void save(Teacher teacher) {
        teacherDAO.save(teacher);
    }

    @Override
    public void deleteById(int id) {
        teacherDAO.deleteById(id);
    }
}

