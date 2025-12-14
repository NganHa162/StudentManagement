package org.example.studentmanagement.service;

import org.example.studentmanagement.dao.AdminDAO;
import org.example.studentmanagement.dao.StudentDAO;
import org.example.studentmanagement.dao.TeacherDAO;
import org.example.studentmanagement.entity.Admin;
import org.example.studentmanagement.entity.Student;
import org.example.studentmanagement.entity.StudentCourseDetails;
import org.example.studentmanagement.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminDAO adminDAO;
    private final StudentDAO studentDAO;
    private final TeacherDAO teacherDAO;
    private final StudentCourseDetailsService studentCourseDetailsService;
    private final GradeDetailsService gradeDetailsService;

    @Autowired
    public AdminServiceImpl(AdminDAO adminDAO, 
                           StudentDAO studentDAO,
                           TeacherDAO teacherDAO,
                           StudentCourseDetailsService studentCourseDetailsService,
                           GradeDetailsService gradeDetailsService) {
        this.adminDAO = adminDAO;
        this.studentDAO = studentDAO;
        this.teacherDAO = teacherDAO;
        this.studentCourseDetailsService = studentCourseDetailsService;
        this.gradeDetailsService = gradeDetailsService;
    }

    @Override
    public Optional<Admin> findByUserName(String userName) {
        return adminDAO.findByUserName(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found: " + username));
        return User.withUsername(admin.getUserName())
                .password(admin.getPassword())
                .roles("ADMIN")
                .build();
    }
    
    // Admin operations on Student entities
    
    @Override
    public void createStudent(Student student) {
        // Initialize courses list if null
        if (student.getCourses() == null) {
            student.setCourses(new ArrayList<>());
        }
        
        studentDAO.save(student);
    }

    @Override
    public void updateStudent(Student student) {
        // Business logic for updating student
        Student existing = studentDAO.findById(student.getId());
        if (existing != null) {
            // Keep courses if not provided
            if (student.getCourses() == null) {
                student.setCourses(existing.getCourses());
            }
        }
        
        studentDAO.save(student);
    }

    @Override
    public void deleteStudentWithRelatedData(int studentId) {
        // Delete all related data before deleting student
        List<StudentCourseDetails> enrollments = studentCourseDetailsService.findByStudentId(studentId);
        
        for(StudentCourseDetails scd : enrollments) {
            int gradeId = scd.getGradeDetails().getId();
            studentCourseDetailsService.deleteByStudentId(studentId);
            gradeDetailsService.deleteById(gradeId);
        }
        
        // Finally delete the student
        studentDAO.deleteById(studentId);
    }
    
    // Admin operations on Teacher entities
    
    @Override
    public void createTeacher(Teacher teacher) {
        // Initialize courses list if null
        if (teacher.getCourses() == null) {
            teacher.setCourses(new ArrayList<>());
        }
        
        teacherDAO.save(teacher);
    }

    @Override
    public void updateTeacher(Teacher teacher) {
        // Business logic for updating teacher
        Teacher existing = teacherDAO.findById(teacher.getId());
        if (existing != null) {
            // Keep courses if not provided
            if (teacher.getCourses() == null) {
                teacher.setCourses(existing.getCourses());
            }
        }
        
        teacherDAO.save(teacher);
    }
}


