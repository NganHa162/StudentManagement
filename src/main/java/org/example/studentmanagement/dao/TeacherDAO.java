package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.Teacher;

import java.util.Optional;

public interface TeacherDAO extends BaseDAO<Teacher, Integer>{
    Optional<Teacher> findByUserName(String userName);
}
