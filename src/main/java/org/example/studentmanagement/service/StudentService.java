package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.Student;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface StudentService extends UserDetailsService {
    Optional<Student> findByUserName(String userName);
}


