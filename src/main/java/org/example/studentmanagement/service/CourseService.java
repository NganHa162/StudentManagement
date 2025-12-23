package org.example.studentmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.studentmanagement.dto.CourseView;
import org.example.studentmanagement.entity.Course;
import org.example.studentmanagement.entity.Student;
import org.example.studentmanagement.repository.CourseRepository;
import org.example.studentmanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CourseService {
    
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    
    /**
     * Get enrolled courses for a student (from class diagram)
     * Returns list of courses that the student is actively enrolled in
     * 
     * @param studentId The ID of the student
     * @return List of Course entities
     */
    public List<Course> getEnrolledCourses(Integer studentId) {
        log.info("Fetching enrolled courses for student ID: {}", studentId);
        
        // Verify student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
        
        // Get courses from repository
        List<Course> courses = courseRepository.findCoursesByStudentId(studentId);
        
        log.info("Found {} courses for student {}", courses.size(), student.getStudentCode());
        return courses;
    }
    
    /**
     * Filter courses by keyword (from class diagram)
     * Searches in course code, course name, and teacher name
     * 
     * @param courses List of courses to filter
     * @param keyword Search keyword
     * @return Filtered list of courses
     */
    public List<Course> filterCourses(List<Course> courses, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return courses;
        }
        
        String lowerKeyword = keyword.toLowerCase().trim();
        log.info("Filtering courses with keyword: {}", lowerKeyword);
        
        return courses.stream()
                .filter(course -> 
                    course.getCourseCode().toLowerCase().contains(lowerKeyword) ||
                    course.getCourseName().toLowerCase().contains(lowerKeyword) ||
                    (course.getTeacher() != null && 
                     course.getTeacher().getFullName().toLowerCase().contains(lowerKeyword))
                )
                .collect(Collectors.toList());
    }
    
    /**
     * Sort courses by option (from class diagram)
     * Supported options: "name", "code", "teacher", "schedule"
     * 
     * @param courses List of courses to sort
     * @param option Sort option
     * @return Sorted list of courses
     */
    public List<Course> sortCourses(List<Course> courses, String option) {
        if (option == null || option.trim().isEmpty()) {
            return courses;
        }
        
        log.info("Sorting courses by: {}", option);
        
        return switch (option.toLowerCase()) {
            case "name" -> courses.stream()
                    .sorted(Comparator.comparing(Course::getCourseName))
                    .collect(Collectors.toList());
                    
            case "code" -> courses.stream()
                    .sorted(Comparator.comparing(Course::getCourseCode))
                    .collect(Collectors.toList());
                    
            case "teacher" -> courses.stream()
                    .sorted(Comparator.comparing(course -> 
                        course.getTeacher() != null ? course.getTeacher().getFullName() : ""))
                    .collect(Collectors.toList());
                    
            case "schedule" -> courses.stream()
                    .sorted(Comparator.comparing(Course::getSchedule, 
                        Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
                    
            default -> {
                log.warn("Invalid sort option: {}. Returning unsorted list.", option);
                yield courses;
            }
        };
    }
    
    /**
     * Convert Course entities to CourseView DTOs
     * 
     * @param courses List of Course entities
     * @return List of CourseView DTOs
     */
    public List<CourseView> convertToViews(List<Course> courses) {
        List<CourseView> views = courses.stream()
                .map(course -> {
                    CourseView view = new CourseView(course);
                    view.setTotalCourses(courses.size());
                    return view;
                })
                .collect(Collectors.toList());
        
        log.info("Converted {} courses to views", views.size());
        return views;
    }
    
    /**
     * Complete workflow: Get enrolled courses, filter, sort, and convert to views
     * This is the main method called by the controller
     * 
     * @param studentId Student ID
     * @param keyword Filter keyword (optional)
     * @param sortOption Sort option (optional)
     * @return List of CourseView DTOs
     */
    public List<CourseView> getStudentCourses(Integer studentId, String keyword, String sortOption) {
        // Step 1: Get enrolled courses
        List<Course> courses = getEnrolledCourses(studentId);
        
        // Step 2: Filter if keyword provided
        if (keyword != null && !keyword.trim().isEmpty()) {
            courses = filterCourses(courses, keyword);
        }
        
        // Step 3: Sort if option provided
        if (sortOption != null && !sortOption.trim().isEmpty()) {
            courses = sortCourses(courses, sortOption);
        }
        
        // Step 4: Convert to views
        return convertToViews(courses);
    }
    
    /**
     * Get course details with teacher information
     * 
     * @param courseId Course ID
     * @return Course with teacher loaded
     */
    public Course getCourseWithTeacher(Integer courseId) {
        return courseRepository.findCourseWithTeacher(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));
    }
}

