package org.example.studentmanagement.repository;

import org.example.studentmanagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    
    Optional<Student> findByStudentCode(String studentCode);
    
    Optional<Student> findByEmail(String email);
}

