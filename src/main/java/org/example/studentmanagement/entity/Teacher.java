package org.example.studentmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Integer teacherId;
    
    @Column(name = "teacher_code", unique = true, nullable = false)
    private String teacherCode;
    
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "department")
    private String department;
    
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();
    
    // Business method from class diagram
    public String getTeacherInfo() {
        return String.format("%s - %s (%s)", fullName, department, teacherCode);
    }
}

