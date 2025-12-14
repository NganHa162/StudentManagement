package org.example.studentmanagement.service;

import org.example.studentmanagement.entity.Admin;
import org.example.studentmanagement.entity.Student;
import org.example.studentmanagement.entity.Teacher;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface AdminService extends UserDetailsService {
    Optional<Admin> findByUserName(String userName);
    
    // Admin operations on Student entities
    void createStudent(Student student);
    void updateStudent(Student student);
    void deleteStudentWithRelatedData(int studentId);
    
    // Admin operations on Teacher entities
    void createTeacher(Teacher teacher);
    void updateTeacher(Teacher teacher);
}


