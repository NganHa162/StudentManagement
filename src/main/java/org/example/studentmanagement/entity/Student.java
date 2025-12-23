package org.example.studentmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Integer studentId;
    
    @Column(name = "student_code", unique = true, nullable = false)
    private String studentCode;
    
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudentCourseDetails> enrollments = new ArrayList<>();
    
    // Business methods from class diagram
    public void login() {
        // Login logic will be implemented in authentication service
    }
    
    public List<Course> viewMyCourses() {
        return enrollments.stream()
                .filter(enrollment -> "ACTIVE".equals(enrollment.getStatus()))
                .map(StudentCourseDetails::getCourse)
                .toList();
    }
    
    public List<Course> filterCourses(String keyword) {
        return viewMyCourses().stream()
                .filter(course -> course.getCourseName().toLowerCase().contains(keyword.toLowerCase()) ||
                        course.getCourseCode().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }
    
    public List<Course> sortCourses(String option) {
        List<Course> courses = viewMyCourses();
        if ("name".equalsIgnoreCase(option)) {
            return courses.stream()
                    .sorted((c1, c2) -> c1.getCourseName().compareTo(c2.getCourseName()))
                    .toList();
        } else if ("code".equalsIgnoreCase(option)) {
            return courses.stream()
                    .sorted((c1, c2) -> c1.getCourseCode().compareTo(c2.getCourseCode()))
                    .toList();
        }
        return courses;
    }
}

