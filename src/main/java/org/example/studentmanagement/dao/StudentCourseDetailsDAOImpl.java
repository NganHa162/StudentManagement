package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.StudentCourseDetails;
import org.example.studentmanagement.entity.GradeDetails;
import org.example.studentmanagement.entity.Assignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentCourseDetailsDAOImpl extends BaseDAOImpl<StudentCourseDetails, Integer> implements StudentCourseDetailsDAO {

    private final DataSource dataSource;

    @Autowired
    public StudentCourseDetailsDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(StudentCourseDetails details) {
        String sql;
        if (details.getId() == 0) {
            // Insert new student course details
            sql = "INSERT INTO student_course_details (student_id, course_id) VALUES (?, ?)";
        } else {
            // Update existing student course details
            sql = "UPDATE student_course_details SET student_id = ?, course_id = ? WHERE id = ?";
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, details.getStudentId());
            pstmt.setInt(2, details.getCourseId());

            if (details.getId() != 0) {
                pstmt.setInt(3, details.getId());
            }

            pstmt.executeUpdate();

            // If it's new, get the generated ID
            if (details.getId() == 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        details.setId(rs.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving student course details: " + e.getMessage(), e);
        }
    }

    @Override
    public List<StudentCourseDetails> findAll() {
        List<StudentCourseDetails> detailsList = new ArrayList<>();
        String sql = "SELECT id, student_id, course_id FROM student_course_details";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                StudentCourseDetails details = mapResultSetToStudentCourseDetails(rs);
                detailsList.add(details);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all student course details: " + e.getMessage(), e);
        }

        return detailsList;
    }

    @Override
    public StudentCourseDetails findById(Integer id) {
        String sql = "SELECT id, student_id, course_id FROM student_course_details WHERE id = ?";
        StudentCourseDetails details = null;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    details = mapResultSetToStudentCourseDetails(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding student course details by id: " + e.getMessage(), e);
        }

        return details;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM student_course_details WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting student course details by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<StudentCourseDetails> findByStudentId(int studentId) {
        List<StudentCourseDetails> detailsList = new ArrayList<>();
        String sql = "SELECT id, student_id, course_id FROM student_course_details WHERE student_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    StudentCourseDetails details = mapResultSetToStudentCourseDetails(rs);
                    detailsList.add(details);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding student course details by student id: " + e.getMessage(), e);
        }

        return detailsList;
    }

    @Override
    public List<StudentCourseDetails> findByCourseId(int courseId) {
        List<StudentCourseDetails> detailsList = new ArrayList<>();
        String sql = "SELECT id, student_id, course_id FROM student_course_details WHERE course_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    StudentCourseDetails details = mapResultSetToStudentCourseDetails(rs);
                    detailsList.add(details);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding student course details by course id: " + e.getMessage(), e);
        }

        return detailsList;
    }

    @Override
    public StudentCourseDetails findByStudentIdAndCourseId(int studentId, int courseId) {
        String sql = "SELECT id, student_id, course_id FROM student_course_details WHERE student_id = ? AND course_id = ?";
        StudentCourseDetails details = null;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    details = mapResultSetToStudentCourseDetails(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding student course details by student and course id: " + e.getMessage(), e);
        }

        return details;
    }

    // Helper method to map ResultSet to StudentCourseDetails object
    private StudentCourseDetails mapResultSetToStudentCourseDetails(ResultSet rs) throws SQLException {
        StudentCourseDetails details = new StudentCourseDetails();
        details.setId(rs.getInt("id"));
        details.setStudentId(rs.getInt("student_id"));
        details.setCourseId(rs.getInt("course_id"));
        // Note: GradeDetails and Assignments will need to be loaded separately via their services
        return details;
    }
}

