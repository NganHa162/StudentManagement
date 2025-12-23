package org.example.studentmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.studentmanagement.entity.Course;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseView {
    
    private String courseCode;
    private String courseName;
    private String teacherName;
    private String schedule;
    private Integer totalCourses;
    
    // Constructor from Course entity for easy mapping
    public CourseView(Course course) {
        this.courseCode = course.getCourseCode();
        this.courseName = course.getCourseName();
        this.teacherName = course.getTeacher() != null ? course.getTeacher().getFullName() : "N/A";
        this.schedule = course.getSchedule();
        this.totalCourses = 1; // Will be set in service layer when needed
    }
    
    // Display methods from class diagram
    public void displayCourseList() {
        System.out.println(String.format("Course: %s - %s | Teacher: %s | Schedule: %s",
                courseCode, courseName, teacherName, schedule));
    }
    
    public void displayEmptyMessage() {
        System.out.println("No courses found for this student.");
    }
    
    public void displayNoResultsMessage() {
        System.out.println("No courses match your search criteria.");
    }
}

