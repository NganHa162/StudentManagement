package org.example.studentmanagement.config;

import org.example.studentmanagement.dao.StudentDAO;
import org.example.studentmanagement.dao.TeacherDAO;
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
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(StudentDAO studentDAO, TeacherDAO teacherDAO, PasswordEncoder passwordEncoder) {
        this.studentDAO = studentDAO;
        this.teacherDAO = teacherDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // DISABLED: Using mock-data.sql from Docker instead
        // DataInitializer is disabled to avoid conflicts with database init scripts
        System.out.println("DataInitializer: Skipped (using mock-data.sql from docker-compose)");
    }

    private void initializeStudents() {
        // Create 3 test students
        // Password: "student123"
        createStudent("student11", "student123", "Nguyen", "Van A", "student1@example.com");
        createStudent("student21", "student123", "Tran", "Thi B", "student2@example.com");
        createStudent("student31", "student123", "Le", "Van C", "student3@example.com");
    }

    private void initializeTeachers() {
        // Create 2 test teachers
        // Password: "teacher123"
        createTeacher("teacher11", "teacher123", "Pham", "Thi D", "teacher1@example.com");
        createTeacher("teacher21", "teacher123", "Hoang", "Van E", "teacher2@example.com");
    }

    private void createStudent(String username, String password, String firstName, String lastName, String email) {
        if (studentDAO.findByUserName(username).isPresent()) {
            return;
        }
        Student student = new Student();
        student.setUserName(username);
        student.setPassword(password); // Don't encode - use plain text for testing
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setCourses(new ArrayList<>());
        studentDAO.save(student);
    }

    private void createTeacher(String username, String password, String firstName, String lastName, String email) {
        if (teacherDAO.findByUserName(username).isPresent()) {
            return;
        }
        Teacher teacher = new Teacher();
        teacher.setUserName(username);
        teacher.setPassword(password); // Don't encode - use plain text for testing
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setEmail(email);
        teacher.setCourses(new ArrayList<>());
        teacherDAO.save(teacher);
    }
}
