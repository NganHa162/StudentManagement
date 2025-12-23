package org.example.studentmanagement.repository;

import org.example.studentmanagement.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    
    /**
     * Find courses by student ID (from class diagram)
     * Returns list of courses that a student is enrolled in with ACTIVE status
     */
    @Query("SELECT c FROM Course c " +
           "JOIN c.enrollments e " +
           "WHERE e.student.studentId = :studentId " +
           "AND e.status = 'ACTIVE'")
    List<Course> findCoursesByStudentId(@Param("studentId") Integer studentId);
    
    /**
     * Find course with teacher information (from class diagram)
     * Returns a specific course with its teacher details loaded
     */
    @Query("SELECT c FROM Course c " +
           "LEFT JOIN FETCH c.teacher " +
           "WHERE c.courseId = :courseId")
    Optional<Course> findCourseWithTeacher(@Param("courseId") Integer courseId);
    
    /**
     * Find course by course code
     */
    Optional<Course> findByCourseCode(String courseCode);
}

