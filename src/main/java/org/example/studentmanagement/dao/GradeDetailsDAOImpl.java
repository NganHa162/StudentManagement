package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.GradeDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GradeDetailsDAOImpl extends BaseDAOImpl<GradeDetails, Integer> implements GradeDetailsDAO {

    private final DataSource dataSource;

    @Autowired
    public GradeDetailsDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(GradeDetails gradeDetails) {
        String sql;
        if (gradeDetails.getId() == 0) {
            // Insert new grade
            sql = "INSERT INTO grade_details (student_id, course_id, assignment_name, score, max_score, grade, feedback, graded_date, graded_by_teacher_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            // Update existing grade
            sql = "UPDATE grade_details SET student_id = ?, course_id = ?, assignment_name = ?, score = ?, max_score = ?, grade = ?, feedback = ?, graded_date = ?, graded_by_teacher_id = ? WHERE id = ?";
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, gradeDetails.getStudentId());
            pstmt.setInt(2, gradeDetails.getCourseId());
            pstmt.setString(3, gradeDetails.getAssignmentName());
            pstmt.setDouble(4, gradeDetails.getScore());
            pstmt.setDouble(5, gradeDetails.getMaxScore());
            pstmt.setString(6, gradeDetails.getGrade());
            pstmt.setString(7, gradeDetails.getFeedback());
            pstmt.setString(8, gradeDetails.getGradedDate());
            pstmt.setInt(9, gradeDetails.getGradedByTeacherId());

            if (gradeDetails.getId() != 0) {
                pstmt.setInt(10, gradeDetails.getId());
            }

            pstmt.executeUpdate();

            // If it's a new grade, get the generated ID
            if (gradeDetails.getId() == 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        gradeDetails.setId(rs.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving grade details: " + e.getMessage(), e);
        }
    }

    @Override
    public List<GradeDetails> findAll() {
        List<GradeDetails> gradesList = new ArrayList<>();
        String sql = "SELECT id, student_id, course_id, assignment_name, score, max_score, grade, feedback, graded_date, graded_by_teacher_id FROM grade_details";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                GradeDetails grade = mapResultSetToGradeDetails(rs);
                gradesList.add(grade);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all grade details: " + e.getMessage(), e);
        }

        return gradesList;
    }

    @Override
    public GradeDetails findById(Integer id) {
        String sql = "SELECT id, student_id, course_id, assignment_name, score, max_score, grade, feedback, graded_date, graded_by_teacher_id FROM grade_details WHERE id = ?";
        GradeDetails gradeDetails = null;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    gradeDetails = mapResultSetToGradeDetails(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding grade details by id: " + e.getMessage(), e);
        }

        return gradeDetails;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM grade_details WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting grade details by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<GradeDetails> findByStudentId(int studentId) {
        List<GradeDetails> gradesList = new ArrayList<>();
        String sql = "SELECT id, student_id, course_id, assignment_name, score, max_score, grade, feedback, graded_date, graded_by_teacher_id FROM grade_details WHERE student_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    GradeDetails grade = mapResultSetToGradeDetails(rs);
                    gradesList.add(grade);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding grade details by student id: " + e.getMessage(), e);
        }

        return gradesList;
    }

    @Override
    public List<GradeDetails> findByCourseId(int courseId) {
        List<GradeDetails> gradesList = new ArrayList<>();
        String sql = "SELECT id, student_id, course_id, assignment_name, score, max_score, grade, feedback, graded_date, graded_by_teacher_id FROM grade_details WHERE course_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    GradeDetails grade = mapResultSetToGradeDetails(rs);
                    gradesList.add(grade);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding grade details by course id: " + e.getMessage(), e);
        }

        return gradesList;
    }

    @Override
    public List<GradeDetails> findByStudentIdAndCourseId(int studentId, int courseId) {
        List<GradeDetails> gradesList = new ArrayList<>();
        String sql = "SELECT id, student_id, course_id, assignment_name, score, max_score, grade, feedback, graded_date, graded_by_teacher_id FROM grade_details WHERE student_id = ? AND course_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    GradeDetails grade = mapResultSetToGradeDetails(rs);
                    gradesList.add(grade);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding grade details by student and course id: " + e.getMessage(), e);
        }

        return gradesList;
    }

    // Helper method to map ResultSet to GradeDetails object
    private GradeDetails mapResultSetToGradeDetails(ResultSet rs) throws SQLException {
        GradeDetails grade = new GradeDetails();
        grade.setId(rs.getInt("id"));
        grade.setStudentId(rs.getInt("student_id"));
        grade.setCourseId(rs.getInt("course_id"));
        grade.setAssignmentName(rs.getString("assignment_name"));
        grade.setScore(rs.getDouble("score"));
        grade.setMaxScore(rs.getDouble("max_score"));
        grade.setGrade(rs.getString("grade"));
        grade.setFeedback(rs.getString("feedback"));
        grade.setGradedDate(rs.getString("graded_date"));
        grade.setGradedByTeacherId(rs.getInt("graded_by_teacher_id"));
        return grade;
    }
}

