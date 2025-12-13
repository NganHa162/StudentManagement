package org.example.studentmanagement.security;

import org.example.studentmanagement.service.AdminService;
import org.example.studentmanagement.service.StudentService;
import org.example.studentmanagement.service.TeacherService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CompositeUserDetailsService implements UserDetailsService {

    private final AdminService adminService;
    private final TeacherService teacherService;
    private final StudentService studentService;

    public CompositeUserDetailsService(AdminService adminService, 
                                      TeacherService teacherService, 
                                      StudentService studentService) {
        this.adminService = adminService;
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try Admin first
        try {
            return adminService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            // Continue to next service
        }

        // Try Teacher
        try {
            return teacherService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            // Continue to next service
        }

        // Try Student
        try {
            return studentService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            // All services failed
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}

