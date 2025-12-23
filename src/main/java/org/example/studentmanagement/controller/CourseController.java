package org.example.studentmanagement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.studentmanagement.dto.ApiResponse;
import org.example.studentmanagement.dto.CourseView;
import org.example.studentmanagement.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Course operations
 * Implements the methods from the class diagram:
 * - viewStudentCourses(studentId: int)
 * - filterCourses(studentId: int, keyword: String)
 * - sortCourses(studentId: int, sortOption: String)
 */
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CourseController {
    
    private final CourseService courseService;
    
    /**
     * View all courses for a student (from class diagram)
     * GET /api/courses/student/{studentId}
     * 
     * @param studentId The ID of the student
     * @return List of CourseView DTOs
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<CourseView>>> viewStudentCourses(
            @PathVariable Integer studentId) {
        try {
            log.info("Received request to view courses for student ID: {}", studentId);
            
            List<CourseView> courses = courseService.getStudentCourses(studentId, null, null);
            
            if (courses.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success("No courses found for this student", courses)
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(
                    String.format("Found %d course(s)", courses.size()), 
                    courses
                )
            );
            
        } catch (RuntimeException e) {
            log.error("Error viewing courses for student {}: {}", studentId, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Filter courses by keyword (from class diagram)
     * GET /api/courses/student/{studentId}/filter?keyword={keyword}
     * 
     * @param studentId The ID of the student
     * @param keyword Search keyword (course code, name, or teacher name)
     * @return Filtered list of CourseView DTOs
     */
    @GetMapping("/student/{studentId}/filter")
    public ResponseEntity<ApiResponse<List<CourseView>>> filterCourses(
            @PathVariable Integer studentId,
            @RequestParam String keyword) {
        try {
            log.info("Filtering courses for student {} with keyword: {}", studentId, keyword);
            
            List<CourseView> courses = courseService.getStudentCourses(studentId, keyword, null);
            
            if (courses.isEmpty()) {
                return ResponseEntity.ok(
                    ApiResponse.success("No courses match your search criteria", courses)
                );
            }
            
            return ResponseEntity.ok(
                ApiResponse.success(
                    String.format("Found %d matching course(s)", courses.size()), 
                    courses
                )
            );
            
        } catch (RuntimeException e) {
            log.error("Error filtering courses: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Sort courses by option (from class diagram)
     * GET /api/courses/student/{studentId}/sort?option={option}
     * 
     * @param studentId The ID of the student
     * @param sortOption Sort option (name, code, teacher, schedule)
     * @return Sorted list of CourseView DTOs
     */
    @GetMapping("/student/{studentId}/sort")
    public ResponseEntity<ApiResponse<List<CourseView>>> sortCourses(
            @PathVariable Integer studentId,
            @RequestParam String sortOption) {
        try {
            log.info("Sorting courses for student {} by: {}", studentId, sortOption);
            
            List<CourseView> courses = courseService.getStudentCourses(studentId, null, sortOption);
            
            return ResponseEntity.ok(
                ApiResponse.success(
                    String.format("Courses sorted by %s", sortOption), 
                    courses
                )
            );
            
        } catch (RuntimeException e) {
            log.error("Error sorting courses: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Combined endpoint: View courses with optional filter and sort
     * GET /api/courses/student/{studentId}/view?keyword={keyword}&sort={sort}
     * 
     * This is a convenience endpoint that combines all operations
     * 
     * @param studentId The ID of the student
     * @param keyword Optional search keyword
     * @param sortOption Optional sort option
     * @return List of CourseView DTOs (filtered and sorted if params provided)
     */
    @GetMapping("/student/{studentId}/view")
    public ResponseEntity<ApiResponse<List<CourseView>>> viewCoursesWithOptions(
            @PathVariable Integer studentId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortOption) {
        try {
            log.info("Viewing courses for student {} (keyword: {}, sort: {})", 
                    studentId, keyword, sortOption);
            
            List<CourseView> courses = courseService.getStudentCourses(
                    studentId, keyword, sortOption);
            
            String message;
            if (courses.isEmpty()) {
                message = keyword != null ? "No courses match your search criteria" 
                                         : "No courses found for this student";
            } else {
                message = String.format("Found %d course(s)", courses.size());
            }
            
            return ResponseEntity.ok(ApiResponse.success(message, courses));
            
        } catch (RuntimeException e) {
            log.error("Error viewing courses: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Get detailed information about a specific course
     * GET /api/courses/{courseId}
     * 
     * @param courseId The ID of the course
     * @return Course details with teacher information
     */
    @GetMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CourseView>> getCourseDetails(
            @PathVariable Integer courseId) {
        try {
            log.info("Fetching details for course ID: {}", courseId);
            
            var course = courseService.getCourseWithTeacher(courseId);
            CourseView view = new CourseView(course);
            
            return ResponseEntity.ok(ApiResponse.success(view));
            
        } catch (RuntimeException e) {
            log.error("Error fetching course details: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

