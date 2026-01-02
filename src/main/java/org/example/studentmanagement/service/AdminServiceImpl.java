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
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final StudentCourseDetailsService studentCourseDetailsService;
    private final GradeDetailsService gradeDetailsService;

    @Autowired
    public AdminServiceImpl(AdminDAO adminDAO,
                           StudentDAO studentDAO,
                           TeacherDAO teacherDAO,
                           StudentService studentService,
                           TeacherService teacherService,
                           StudentCourseDetailsService studentCourseDetailsService,
                           GradeDetailsService gradeDetailsService) {
        this.adminDAO = adminDAO;
        this.studentDAO = studentDAO;
        this.teacherDAO = teacherDAO;
        this.studentService = studentService;
        this.teacherService = teacherService;
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

        // Use StudentService.save() which handles password encoding
        studentService.save(student);
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

        // Use StudentService.save() which handles password encoding
        studentService.save(student);
    }

    @Override
    public void deleteStudentWithRelatedData(int studentId) {
        // Delete all grade details for this student
        List<StudentCourseDetails> enrollments = studentCourseDetailsService.findByStudentId(studentId);
        for(StudentCourseDetails scd : enrollments) {
            // Find and delete all grades for this student and course
            List<org.example.studentmanagement.entity.GradeDetails> grades =
                gradeDetailsService.findByStudentIdAndCourseId(studentId, scd.getCourseId());
            for(org.example.studentmanagement.entity.GradeDetails grade : grades) {
                gradeDetailsService.deleteById(grade.getId());
            }
        }

        // Delete all student course enrollments
        studentCourseDetailsService.deleteByStudentId(studentId);

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

        // Use TeacherService.save() which handles password encoding
        teacherService.save(teacher);
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

        // Use TeacherService.save() which handles password encoding
        teacherService.save(teacher);
    }
}


