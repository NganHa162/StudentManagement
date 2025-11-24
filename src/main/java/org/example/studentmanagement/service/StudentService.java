package org.example.studentmanagement.service;

import java.util.Optional;

import org.example.studentmanagement.entity.Student;

public interface StudentService {

    Optional<Student> findByUsername(String username);

    Optional<Student> findById(Long id);
}

