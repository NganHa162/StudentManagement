package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.AssignmentDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AssignmentDetailsDAOImpl extends BaseDAOImpl<AssignmentDetails, Integer> implements AssignmentDetailsDAO {

    private final DataSource dataSource;

    @Autowired
    public AssignmentDetailsDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(AssignmentDetails details) {
        String sql;
        if (details.getId() == 0) {
            sql = "INSERT INTO assignment_details (assignment_id, student_course_details_id, is_done) VALUES (?, ?, ?)";
        } else {
            sql = "UPDATE assignment_details SET assignment_id = ?, student_course_details_id = ?, is_done = ? WHERE id = ?";
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, details.getAssignmentId());
            pstmt.setInt(2, details.getStudentCourseDetailsId());
            pstmt.setInt(3, details.getIsDone());

            if (details.getId() != 0) {
                pstmt.setInt(4, details.getId());
            }

            pstmt.executeUpdate();

            if (details.getId() == 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        details.setId(rs.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving assignment details: " + e.getMessage(), e);
        }
    }

    @Override
    public List<AssignmentDetails> findAll() {
        List<AssignmentDetails> detailsList = new ArrayList<>();
        String sql = "SELECT id, assignment_id, student_course_details_id, is_done FROM assignment_details";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                AssignmentDetails details = mapResultSetToAssignmentDetails(rs);
                detailsList.add(details);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all assignment details: " + e.getMessage(), e);
        }

        return detailsList;
    }

    @Override
    public AssignmentDetails findById(Integer id) {
        String sql = "SELECT id, assignment_id, student_course_details_id, is_done FROM assignment_details WHERE id = ?";
        AssignmentDetails details = null;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    details = mapResultSetToAssignmentDetails(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding assignment details by id: " + e.getMessage(), e);
        }

        return details;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM assignment_details WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting assignment details by id: " + e.getMessage(), e);
        }
    }

    @Override
    public AssignmentDetails findByAssignmentIdAndStudentCourseDetailsId(int assignmentId, int studentCourseDetailsId) {
        String sql = "SELECT id, assignment_id, student_course_details_id, is_done FROM assignment_details WHERE assignment_id = ? AND student_course_details_id = ?";
        AssignmentDetails details = null;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, assignmentId);
            pstmt.setInt(2, studentCourseDetailsId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    details = mapResultSetToAssignmentDetails(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding assignment details by assignment and student course details id: " + e.getMessage(), e);
        }

        return details;
    }

    @Override
    public List<AssignmentDetails> findByAssignmentId(int assignmentId) {
        List<AssignmentDetails> detailsList = new ArrayList<>();
        String sql = "SELECT id, assignment_id, student_course_details_id, is_done FROM assignment_details WHERE assignment_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, assignmentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    AssignmentDetails details = mapResultSetToAssignmentDetails(rs);
                    detailsList.add(details);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding assignment details by assignment id: " + e.getMessage(), e);
        }

        return detailsList;
    }

    @Override
    public List<AssignmentDetails> findByStudentCourseDetailsId(int studentCourseDetailsId) {
        List<AssignmentDetails> detailsList = new ArrayList<>();
        String sql = "SELECT id, assignment_id, student_course_details_id, is_done FROM assignment_details WHERE student_course_details_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentCourseDetailsId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    AssignmentDetails details = mapResultSetToAssignmentDetails(rs);
                    detailsList.add(details);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding assignment details by student course details id: " + e.getMessage(), e);
        }

        return detailsList;
    }

    private AssignmentDetails mapResultSetToAssignmentDetails(ResultSet rs) throws SQLException {
        AssignmentDetails details = new AssignmentDetails();
        details.setId(rs.getInt("id"));
        details.setAssignmentId(rs.getInt("assignment_id"));
        details.setStudentCourseDetailsId(rs.getInt("student_course_details_id"));
        details.setIsDone(rs.getInt("is_done"));
        return details;
    }
}

