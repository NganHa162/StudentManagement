package org.example.studentmanagement.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import org.example.studentmanagement.entity.GradeDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

class GradeDetailsDAOImplTest {

    private EmbeddedDatabase dataSource;
    private GradeDetailsDAOImpl gradeDetailsDAO;

    @BeforeEach
    void setUp() throws Exception {
        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE grade_details (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "student_id INT NOT NULL, " +
                    "course_id INT NOT NULL, " +
                    "assignment_name VARCHAR(255), " +
                    "score DOUBLE, " +
                    "max_score DOUBLE, " +
                    "grade VARCHAR(10), " +
                    "feedback TEXT, " +
                    "graded_date VARCHAR(255), " +
                    "graded_by_teacher_id INT" +
                    ")");
        }

        gradeDetailsDAO = new GradeDetailsDAOImpl(dataSource);
    }

    @AfterEach
    void tearDown() {
        if (dataSource != null) {
            dataSource.shutdown();
        }
    }

    @Test
    void save_insertsNewGradeDetails() {
        GradeDetails grade = new GradeDetails();
        grade.setStudentId(101);
        grade.setCourseId(201);
        grade.setAssignmentName("Midterm Exam");
        grade.setScore(85.0);
        grade.setMaxScore(100.0);
        grade.setGrade("B");
        grade.setFeedback("Good work");
        grade.setGradedDate("2025-12-01");
        grade.setGradedByTeacherId(10);

        gradeDetailsDAO.save(grade);

        assertNotEquals(0, grade.getId(), "Grade ID should be set after save");
        GradeDetails found = gradeDetailsDAO.findById(grade.getId());
        assertNotNull(found, "Saved grade should be retrievable");
        assertEquals(101, found.getStudentId());
        assertEquals(201, found.getCourseId());
        assertEquals("Midterm Exam", found.getAssignmentName());
        assertEquals(85.0, found.getScore());
        assertEquals(100.0, found.getMaxScore());
        assertEquals("B", found.getGrade());
        assertEquals("Good work", found.getFeedback());
        assertEquals("2025-12-01", found.getGradedDate());
        assertEquals(10, found.getGradedByTeacherId());
    }

    @Test
    void save_updatesExistingGradeDetails() {
        GradeDetails grade = new GradeDetails();
        grade.setStudentId(102);
        grade.setCourseId(202);
        grade.setAssignmentName("Quiz 1");
        grade.setScore(70.0);
        grade.setMaxScore(100.0);
        grade.setGrade("C");
        grade.setFeedback("Needs improvement");
        grade.setGradedDate("2025-11-15");
        grade.setGradedByTeacherId(11);
        gradeDetailsDAO.save(grade);

        grade.setScore(90.0);
        grade.setGrade("A");
        grade.setFeedback("Excellent improvement");
        gradeDetailsDAO.save(grade);

        GradeDetails updated = gradeDetailsDAO.findById(grade.getId());
        assertNotNull(updated);
        assertEquals(90.0, updated.getScore());
        assertEquals("A", updated.getGrade());
        assertEquals("Excellent improvement", updated.getFeedback());
        assertEquals("Quiz 1", updated.getAssignmentName());
    }

    @Test
    void findAll_returnsAllGradeDetails() {
        GradeDetails grade1 = new GradeDetails();
        grade1.setStudentId(101);
        grade1.setCourseId(201);
        grade1.setAssignmentName("Assignment 1");
        grade1.setScore(80.0);
        grade1.setMaxScore(100.0);
        grade1.setGrade("B");
        grade1.setFeedback("Good");
        grade1.setGradedDate("2025-12-01");
        grade1.setGradedByTeacherId(10);

        GradeDetails grade2 = new GradeDetails();
        grade2.setStudentId(102);
        grade2.setCourseId(202);
        grade2.setAssignmentName("Assignment 2");
        grade2.setScore(95.0);
        grade2.setMaxScore(100.0);
        grade2.setGrade("A");
        grade2.setFeedback("Excellent");
        grade2.setGradedDate("2025-12-02");
        grade2.setGradedByTeacherId(11);

        gradeDetailsDAO.save(grade1);
        gradeDetailsDAO.save(grade2);

        List<GradeDetails> grades = gradeDetailsDAO.findAll();

        assertEquals(2, grades.size(), "Should return all grade details");
        assertTrue(grades.stream().anyMatch(g -> g.getAssignmentName().equals("Assignment 1")));
        assertTrue(grades.stream().anyMatch(g -> g.getAssignmentName().equals("Assignment 2")));
    }

    @Test
    void findAll_returnsEmptyListWhenNoGrades() {
        List<GradeDetails> grades = gradeDetailsDAO.findAll();

        assertNotNull(grades);
        assertEquals(0, grades.size(), "Should return empty list when no grades exist");
    }

    @Test
    void findById_returnsGradeDetailsWhenExists() {
        GradeDetails grade = new GradeDetails();
        grade.setStudentId(103);
        grade.setCourseId(203);
        grade.setAssignmentName("Final Exam");
        grade.setScore(88.0);
        grade.setMaxScore(100.0);
        grade.setGrade("B+");
        grade.setFeedback("Well done");
        grade.setGradedDate("2025-12-10");
        grade.setGradedByTeacherId(12);
        gradeDetailsDAO.save(grade);

        GradeDetails found = gradeDetailsDAO.findById(grade.getId());

        assertNotNull(found);
        assertEquals(grade.getId(), found.getId());
        assertEquals(103, found.getStudentId());
        assertEquals("Final Exam", found.getAssignmentName());
        assertEquals(88.0, found.getScore());
    }

    @Test
    void findById_returnsNullWhenGradeDetailsDoesNotExist() {
        GradeDetails found = gradeDetailsDAO.findById(999);

        assertNull(found, "Should return null when grade details does not exist");
    }

    @Test
    void deleteById_removesGradeDetails() {
        GradeDetails grade = new GradeDetails();
        grade.setStudentId(104);
        grade.setCourseId(204);
        grade.setAssignmentName("Project");
        grade.setScore(92.0);
        grade.setMaxScore(100.0);
        grade.setGrade("A");
        grade.setFeedback("Outstanding");
        grade.setGradedDate("2025-12-05");
        grade.setGradedByTeacherId(13);
        gradeDetailsDAO.save(grade);
        int gradeId = grade.getId();

        gradeDetailsDAO.deleteById(gradeId);

        GradeDetails found = gradeDetailsDAO.findById(gradeId);
        assertNull(found, "Grade details should be deleted");
    }

    @Test
    void deleteById_doesNotThrowExceptionWhenGradeDetailsDoesNotExist() {
        assertDoesNotThrow(() -> gradeDetailsDAO.deleteById(999),
                "Deleting non-existent grade details should not throw exception");
    }

    @Test
    void findByStudentId_returnsAllGradesForStudent() {
        GradeDetails grade1 = new GradeDetails();
        grade1.setStudentId(101);
        grade1.setCourseId(201);
        grade1.setAssignmentName("Quiz 1");
        grade1.setScore(85.0);
        grade1.setMaxScore(100.0);
        grade1.setGrade("B");
        grade1.setFeedback("Good");
        grade1.setGradedDate("2025-11-01");
        grade1.setGradedByTeacherId(10);

        GradeDetails grade2 = new GradeDetails();
        grade2.setStudentId(101);
        grade2.setCourseId(202);
        grade2.setAssignmentName("Quiz 2");
        grade2.setScore(90.0);
        grade2.setMaxScore(100.0);
        grade2.setGrade("A");
        grade2.setFeedback("Excellent");
        grade2.setGradedDate("2025-11-15");
        grade2.setGradedByTeacherId(11);

        GradeDetails grade3 = new GradeDetails();
        grade3.setStudentId(102);
        grade3.setCourseId(201);
        grade3.setAssignmentName("Quiz 1");
        grade3.setScore(75.0);
        grade3.setMaxScore(100.0);
        grade3.setGrade("C");
        grade3.setFeedback("Needs work");
        grade3.setGradedDate("2025-11-01");
        grade3.setGradedByTeacherId(10);

        gradeDetailsDAO.save(grade1);
        gradeDetailsDAO.save(grade2);
        gradeDetailsDAO.save(grade3);

        List<GradeDetails> student101Grades = gradeDetailsDAO.findByStudentId(101);

        assertEquals(2, student101Grades.size(), "Should return all grades for student 101");
        assertTrue(student101Grades.stream().allMatch(g -> g.getStudentId() == 101));
    }

    @Test
    void findByStudentId_returnsEmptyListWhenNoGrades() {
        List<GradeDetails> grades = gradeDetailsDAO.findByStudentId(999);

        assertNotNull(grades);
        assertEquals(0, grades.size(), "Should return empty list when student has no grades");
    }

    @Test
    void findByCourseId_returnsAllGradesForCourse() {
        GradeDetails grade1 = new GradeDetails();
        grade1.setStudentId(101);
        grade1.setCourseId(201);
        grade1.setAssignmentName("Midterm");
        grade1.setScore(85.0);
        grade1.setMaxScore(100.0);
        grade1.setGrade("B");
        grade1.setFeedback("Good");
        grade1.setGradedDate("2025-11-10");
        grade1.setGradedByTeacherId(10);

        GradeDetails grade2 = new GradeDetails();
        grade2.setStudentId(102);
        grade2.setCourseId(201);
        grade2.setAssignmentName("Midterm");
        grade2.setScore(78.0);
        grade2.setMaxScore(100.0);
        grade2.setGrade("C+");
        grade2.setFeedback("Satisfactory");
        grade2.setGradedDate("2025-11-10");
        grade2.setGradedByTeacherId(10);

        GradeDetails grade3 = new GradeDetails();
        grade3.setStudentId(101);
        grade3.setCourseId(202);
        grade3.setAssignmentName("Midterm");
        grade3.setScore(92.0);
        grade3.setMaxScore(100.0);
        grade3.setGrade("A");
        grade3.setFeedback("Excellent");
        grade3.setGradedDate("2025-11-12");
        grade3.setGradedByTeacherId(11);

        gradeDetailsDAO.save(grade1);
        gradeDetailsDAO.save(grade2);
        gradeDetailsDAO.save(grade3);

        List<GradeDetails> course201Grades = gradeDetailsDAO.findByCourseId(201);

        assertEquals(2, course201Grades.size(), "Should return all grades for course 201");
        assertTrue(course201Grades.stream().allMatch(g -> g.getCourseId() == 201));
    }

    @Test
    void findByCourseId_returnsEmptyListWhenNoGrades() {
        List<GradeDetails> grades = gradeDetailsDAO.findByCourseId(999);

        assertNotNull(grades);
        assertEquals(0, grades.size(), "Should return empty list when course has no grades");
    }

    @Test
    void findByStudentIdAndCourseId_returnsCorrectGrades() {
        GradeDetails grade1 = new GradeDetails();
        grade1.setStudentId(101);
        grade1.setCourseId(201);
        grade1.setAssignmentName("Assignment 1");
        grade1.setScore(85.0);
        grade1.setMaxScore(100.0);
        grade1.setGrade("B");
        grade1.setFeedback("Good");
        grade1.setGradedDate("2025-11-01");
        grade1.setGradedByTeacherId(10);

        GradeDetails grade2 = new GradeDetails();
        grade2.setStudentId(101);
        grade2.setCourseId(201);
        grade2.setAssignmentName("Assignment 2");
        grade2.setScore(90.0);
        grade2.setMaxScore(100.0);
        grade2.setGrade("A");
        grade2.setFeedback("Excellent");
        grade2.setGradedDate("2025-11-15");
        grade2.setGradedByTeacherId(10);

        GradeDetails grade3 = new GradeDetails();
        grade3.setStudentId(101);
        grade3.setCourseId(202);
        grade3.setAssignmentName("Assignment 1");
        grade3.setScore(88.0);
        grade3.setMaxScore(100.0);
        grade3.setGrade("B+");
        grade3.setFeedback("Very good");
        grade3.setGradedDate("2025-11-05");
        grade3.setGradedByTeacherId(11);

        GradeDetails grade4 = new GradeDetails();
        grade4.setStudentId(102);
        grade4.setCourseId(201);
        grade4.setAssignmentName("Assignment 1");
        grade4.setScore(75.0);
        grade4.setMaxScore(100.0);
        grade4.setGrade("C");
        grade4.setFeedback("Needs work");
        grade4.setGradedDate("2025-11-01");
        grade4.setGradedByTeacherId(10);

        gradeDetailsDAO.save(grade1);
        gradeDetailsDAO.save(grade2);
        gradeDetailsDAO.save(grade3);
        gradeDetailsDAO.save(grade4);

        List<GradeDetails> student101Course201Grades = gradeDetailsDAO.findByStudentIdAndCourseId(101, 201);

        assertEquals(2, student101Course201Grades.size(), "Should return all grades for student 101 in course 201");
        assertTrue(student101Course201Grades.stream().allMatch(g -> g.getStudentId() == 101 && g.getCourseId() == 201));
    }

    @Test
    void findByStudentIdAndCourseId_returnsEmptyListWhenNoGrades() {
        List<GradeDetails> grades = gradeDetailsDAO.findByStudentIdAndCourseId(999, 999);

        assertNotNull(grades);
        assertEquals(0, grades.size(), "Should return empty list when no grades exist for student and course");
    }
}
