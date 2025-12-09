package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.Teacher;
import java.util.List;

public interface TeacherService {
    Teacher findByTeacherId(int id);
    List<Teacher> findAllTeachers();
    void save(Teacher teacher);
    void deleteById(int id);
}

