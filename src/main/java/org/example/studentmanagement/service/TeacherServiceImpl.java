package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.TeacherDAO;
import org.example.studentmanagement.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherDAO teacherDAO;

    @Autowired
    public TeacherServiceImpl(TeacherDAO teacherDAO) {
        this.teacherDAO = teacherDAO;
    }

    @Override
    public Teacher findByTeacherId(int id) {
        return teacherDAO.findById(id);
    }

    @Override
    public List<Teacher> findAllTeachers() {
        return teacherDAO.findAll();
    }

    @Override
    public void save(Teacher teacher) {
        teacherDAO.save(teacher);
    }

    @Override
    public void deleteById(int id) {
        teacherDAO.deleteById(id);
    }

    @Override
    public Optional<Teacher> findByUserName(String userName) {
        return teacherDAO.findByUserName(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Teacher teacher = findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Teacher not found: " + username));
        return User.withUsername(teacher.getUserName())
                .password(teacher.getPassword())
                .roles("TEACHER")
                .build();
    }
}

