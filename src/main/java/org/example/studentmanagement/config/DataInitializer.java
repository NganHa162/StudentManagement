package org.example.studentmanagement.config;

import org.example.studentmanagement.dao.AdminDAO;
import org.example.studentmanagement.dao.StudentDAO;
import org.example.studentmanagement.dao.TeacherDAO;
import org.example.studentmanagement.entity.Admin;
import org.example.studentmanagement.entity.Student;
import org.example.studentmanagement.entity.Teacher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Automatically creates test data when the application starts
 * Only creates data if tables are empty
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final StudentDAO studentDAO;
    private final TeacherDAO teacherDAO;
    private final AdminDAO adminDAO;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(StudentDAO studentDAO, TeacherDAO teacherDAO, AdminDAO adminDAO, PasswordEncoder passwordEncoder) {
        this.studentDAO = studentDAO;
        this.teacherDAO = teacherDAO;
        this.adminDAO = adminDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            // Only create data if tables are empty
            if (studentDAO.findAll().isEmpty()) {
                initializeStudents();
                System.out.println("Test data created for Students");
            }

            if (teacherDAO.findAll().isEmpty()) {
                initializeTeachers();
                System.out.println("Test data created for Teachers");
            }

            if (adminDAO.findAll().isEmpty()) {
                initializeAdmins();
                System.out.println("Test data created for Admins");
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not initialize test data. Database connection may not be available.");
            System.err.println("Error: " + e.getMessage());
            // Don't throw exception to allow application to continue
        }
    }

    private void initializeStudents() {
        // Create 3 test students
        // Password: "student123"
        createStudent("student1", "student123", "Nguyen", "Van A", "student1@example.com");
        createStudent("student2", "student123", "Tran", "Thi B", "student2@example.com");
        createStudent("student3", "student123", "Le", "Van C", "student3@example.com");
    }

    private void initializeTeachers() {
        // Create 2 test teachers
        // Password: "teacher123"
        createTeacher("teacher1", "teacher123", "Pham", "Thi D", "teacher1@example.com");
        createTeacher("teacher2", "teacher123", "Hoang", "Van E", "teacher2@example.com");
    }

    private void initializeAdmins() {
        // Create 1 test admin
        // Password: "admin123"
        createAdmin("admin", "admin123", "Admin", "User", "admin@example.com");
    }

    private void createStudent(String username, String password, String firstName, String lastName, String email) {
        Student student = new Student();
        student.setUserName(username);
        student.setPassword(password);  // Don't encode - use plain text for testing
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setCourses(new ArrayList<>());
        studentDAO.save(student);
    }

    private void createTeacher(String username, String password, String firstName, String lastName, String email) {
        Teacher teacher = new Teacher();
        teacher.setUserName(username);
        teacher.setPassword(password);  // Don't encode - use plain text for testing
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setEmail(email);
        teacher.setCourses(new ArrayList<>());
        teacherDAO.save(teacher);
    }

    private void createAdmin(String username, String password, String firstName, String lastName, String email) {
        Admin admin = new Admin();
        admin.setUserName(username);
        admin.setPassword(password);  // Don't encode - use plain text for testing
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(email);
        adminDAO.save(admin);
    }
}

