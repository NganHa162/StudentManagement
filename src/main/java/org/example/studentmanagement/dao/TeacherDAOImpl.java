package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.Teacher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TeacherDAOImpl extends BaseDAOImpl<Teacher, Integer> implements TeacherDAO{

    private final List<Teacher> teachers = new ArrayList<>();

    public TeacherDAOImpl(PasswordEncoder passwordEncoder) {
        teachers.add(new Teacher(200, "teacher1", passwordEncoder.encode("password"),
                "Hung", "Pham", "hung.pham@example.com", new ArrayList<>()));
        teachers.add(new Teacher(201, "teacher2", passwordEncoder.encode("password"),
                "An", "Hoang", "an.hoang@example.com", new ArrayList<>()));
    }

    @Override
    public void save(Teacher entity) {
        teachers.removeIf(teacher -> teacher.getId() == entity.getId());
        teachers.add(entity);
    }

    @Override
    public Teacher findById(Integer id) {
        return teachers.stream()
                .filter(teacher -> teacher.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Teacher> findAll() {
        return List.copyOf(teachers);
    }

    @Override
    public void deleteById(Integer id) {
        teachers.removeIf(teacher -> teacher.getId() == id);
    }

    @Override
    public void delete(Teacher entity) {
        teachers.remove(entity);
    }

    @Override
    public Optional<Teacher> findByUserName(String userName) {
        return teachers.stream()
                .filter(teacher -> teacher.getUserName().equalsIgnoreCase(userName))
                .findFirst();
    }
}
