package org.example.studentmanagement.repository;

import org.example.studentmanagement.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    
    Optional<Teacher> findByTeacherCode(String teacherCode);
    
    Optional<Teacher> findByEmail(String email);
}

