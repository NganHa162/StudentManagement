package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.TeacherDAO;
import org.example.studentmanagement.entity.Teacher;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherDAO teacherDAO;

    public TeacherServiceImpl(TeacherDAO teacherDAO) {
        this.teacherDAO = teacherDAO;
    }

    @Override
    public Optional<Teacher> findByUserName(String userName) {
        return teacherDAO.findByUserName(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Teacher teacher = findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy giáo viên: " + username));
        return User.withUsername(teacher.getUserName())
                .password(teacher.getPassword())
                .roles("TEACHER")
                .build();
    }
}


