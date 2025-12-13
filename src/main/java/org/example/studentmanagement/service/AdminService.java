package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.Admin;
import org.example.studentmanagement.entity.Student;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface AdminService extends UserDetailsService {
    Optional<Admin> findByUserName(String userName);
    void createStudent(Student student);
    void updateStudent(Student student);
    void deleteStudentWithRelatedData(int studentId);
}


