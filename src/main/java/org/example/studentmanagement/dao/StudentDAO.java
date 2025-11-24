package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.Student;

import java.util.Optional;

public interface StudentDAO extends BaseDAO<Student, Integer> {
    Optional<Student> findByUserName(String userName);
}


