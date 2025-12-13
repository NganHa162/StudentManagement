package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.Student;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Optional<Student> findByUserName(String userName);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    Student findByStudentId(int id);
    List<Student> findAllStudents();
    void save(Student student);
    void deleteById(int id);
}
