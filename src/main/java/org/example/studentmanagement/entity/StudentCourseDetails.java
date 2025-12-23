package org.example.studentmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "student_course_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Integer enrollmentId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;
    
    @Column(name = "status")
    private String status; // ACTIVE, COMPLETED, DROPPED
    
    // Business method from class diagram
    public String getEnrollmentInfo() {
        return String.format("Student: %s enrolled in Course: %s on %s - Status: %s",
                student.getStudentCode(),
                course.getCourseCode(),
                enrollmentDate,
                status);
    }
}

