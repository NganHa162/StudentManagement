package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.Teacher;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface TeacherService extends UserDetailsService {
    Teacher findByTeacherId(int id);
    List<Teacher> findAllTeachers();
    void save(Teacher teacher);
    void deleteById(int id);
    void deleteTeacherById(int id);
    Optional<Teacher> findByUserName(String userName);
}

