package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.Teacher;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface TeacherService extends UserDetailsService {
    Optional<Teacher> findByUserName(String userName);
}


