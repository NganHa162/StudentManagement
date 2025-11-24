package org.example.studentmanagement.repository;

import java.util.List;

import org.example.studentmanagement.dto.StudentAssignmentRow;
import org.example.studentmanagement.entity.AssignmentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssignmentDetailsRepository extends JpaRepository<AssignmentDetails, Long> {

@Query("SELECT new org.example.studentmanagement.dto.StudentAssignmentRow(" +
        "ad.assignment.id, ad.assignment.title, ad.assignment.description, ad.assignment.dueDate, " +
        "ad.assignment.course.name, ad.done) " +
        "FROM AssignmentDetails ad " +
        "WHERE ad.enrollment.student.id = :studentId " +
        "AND (:courseName IS NULL OR LOWER(ad.assignment.course.name) LIKE LOWER(CONCAT('%', :courseName, '%')))")
List<StudentAssignmentRow> findAssignmentsForStudent(@Param("studentId") Long studentId,
        @Param("courseName") String courseName);
}

