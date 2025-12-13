package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.StudentDAO;
import org.example.studentmanagement.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO;
    @Override
    public Optional<Student> findByUserName(String userName) {
        return studentDAO.findByUserName(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found: " + username));
        return User.withUsername(student.getUserName())
                .password(student.getPassword())
                .roles("STUDENT")
                .build();
    }

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
