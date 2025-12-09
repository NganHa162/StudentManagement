package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.StudentDAO;
import org.example.studentmanagement.entity.Student;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO;

    public StudentServiceImpl(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

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
}


