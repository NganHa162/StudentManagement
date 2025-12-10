package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.Student;
import java.util.List;

public interface StudentService {
    Student findByStudentId(int id);
    List<Student> findAllStudents();
    void save(Student student);
    void deleteById(int id);
}
