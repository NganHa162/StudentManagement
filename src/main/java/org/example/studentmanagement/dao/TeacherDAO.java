package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.Teacher;

public interface TeacherDAO extends BaseDAO<Teacher, Integer>{
    // Inherits all CRUD methods from BaseDAO
    // Can add custom methods here if needed, for example:
    // Teacher findByUsername(String username);
    // Teacher findByEmail(String email);
}
