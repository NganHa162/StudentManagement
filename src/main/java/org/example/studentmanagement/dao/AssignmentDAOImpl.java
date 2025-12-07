package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.Assignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AssignmentDAOImpl extends BaseDAOImpl<Assignment, Integer> implements AssignmentDAO {

    private final DataSource dataSource;

    @Autowired
    public AssignmentDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Assignment assignment) {
        String sql;
        if (assignment.getId() == 0) {
            // Insert new assignment
            sql = "INSERT INTO assignments (course_id, title, description, due_date, max_score, created_date, status, created_by_teacher_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            // Update existing assignment
            sql = "UPDATE assignments SET course_id = ?, title = ?, description = ?, due_date = ?, max_score = ?, created_date = ?, status = ?, created_by_teacher_id = ? WHERE id = ?";
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, assignment.getCourseId());
            pstmt.setString(2, assignment.getTitle());
            pstmt.setString(3, assignment.getDescription());
            pstmt.setString(4, assignment.getDueDate());
            pstmt.setDouble(5, assignment.getMaxScore());
            pstmt.setString(6, assignment.getCreatedDate());
            pstmt.setString(7, assignment.getStatus());
            pstmt.setInt(8, assignment.getCreatedByTeacherId());

            if (assignment.getId() != 0) {
                pstmt.setInt(9, assignment.getId());
            }

            pstmt.executeUpdate();

            // If it's a new assignment, get the generated ID
            if (assignment.getId() == 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        assignment.setId(rs.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving assignment: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Assignment> findAll() {
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT id, course_id, title, description, due_date, max_score, created_date, status, created_by_teacher_id FROM assignments";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Assignment assignment = mapResultSetToAssignment(rs);
                assignments.add(assignment);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all assignments: " + e.getMessage(), e);
        }

        return assignments;
    }

    @Override
    public Assignment findById(Integer id) {
        String sql = "SELECT id, course_id, title, description, due_date, max_score, created_date, status, created_by_teacher_id FROM assignments WHERE id = ?";
        Assignment assignment = null;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    assignment = mapResultSetToAssignment(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding assignment by id: " + e.getMessage(), e);
        }

        return assignment;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM assignments WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting assignment by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Assignment> findByCourseId(int courseId) {
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT id, course_id, title, description, due_date, max_score, created_date, status, created_by_teacher_id FROM assignments WHERE course_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Assignment assignment = mapResultSetToAssignment(rs);
                    assignments.add(assignment);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding assignments by course id: " + e.getMessage(), e);
        }

        return assignments;
    }

    @Override
    public List<Assignment> findByStatus(String status) {
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT id, course_id, title, description, due_date, max_score, created_date, status, created_by_teacher_id FROM assignments WHERE status = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Assignment assignment = mapResultSetToAssignment(rs);
                    assignments.add(assignment);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding assignments by status: " + e.getMessage(), e);
        }

        return assignments;
    }

    @Override
    public List<Assignment> findByCourseIdAndStatus(int courseId, String status) {
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT id, course_id, title, description, due_date, max_score, created_date, status, created_by_teacher_id FROM assignments WHERE course_id = ? AND status = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            pstmt.setString(2, status);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Assignment assignment = mapResultSetToAssignment(rs);
                    assignments.add(assignment);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding assignments by course id and status: " + e.getMessage(), e);
        }

        return assignments;
    }

    // Helper method to map ResultSet to Assignment object
    private Assignment mapResultSetToAssignment(ResultSet rs) throws SQLException {
        Assignment assignment = new Assignment();
        assignment.setId(rs.getInt("id"));
        assignment.setCourseId(rs.getInt("course_id"));
        assignment.setTitle(rs.getString("title"));
        assignment.setDescription(rs.getString("description"));
        assignment.setDueDate(rs.getString("due_date"));
        assignment.setMaxScore(rs.getDouble("max_score"));
        assignment.setCreatedDate(rs.getString("created_date"));
        assignment.setStatus(rs.getString("status"));
        assignment.setCreatedByTeacherId(rs.getInt("created_by_teacher_id"));
        return assignment;
    }
}

