package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.StudentDAO;
import org.example.studentmanagement.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO;

    @Autowired
    public StudentServiceImpl(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Override
    public Student findByStudentId(int id) {
        return studentDAO.findById(id);
    }

    @Override
    public List<Student> findAllStudents() {
        return studentDAO.findAll();
    }

    @Override
    public void save(Student student) {
        studentDAO.save(student);
    }

    @Override
    public void deleteById(int id) {
        studentDAO.deleteById(id);
    }
}
