package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.Student;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentDAOImpl extends BaseDAOImpl<Student, Integer> implements StudentDAO {

    private final List<Student> students = new ArrayList<>();

    public StudentDAOImpl(PasswordEncoder passwordEncoder) {
        students.add(new Student(1, "student1", passwordEncoder.encode("password"), "Minh", "Tran",
                "minh.tran@example.com", new ArrayList<>()));
        students.add(new Student(2, "student2", passwordEncoder.encode("password"), "Lan", "Nguyen",
                "lan.nguyen@example.com", new ArrayList<>()));
    }

    @Override
    public void save(Student entity) {
        students.removeIf(st -> st.getId() == entity.getId());
        students.add(entity);
    }

    @Override
    public Student findById(Integer id) {
        return students.stream()
                .filter(student -> student.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Student> findAll() {
        return List.copyOf(students);
    }

    @Override
    public void deleteById(Integer id) {
        students.removeIf(student -> student.getId() == id);
    }

    @Override
    public void delete(Student entity) {
        students.remove(entity);
    }

    @Override
    public Optional<Student> findByUserName(String userName) {
        return students.stream()
                .filter(student -> student.getUserName().equalsIgnoreCase(userName))
                .findFirst();
    }
}


