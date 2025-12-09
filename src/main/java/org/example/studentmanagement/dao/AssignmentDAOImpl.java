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
            sql = "INSERT INTO assignments (title, description, due_date, course_id, days_remaining) VALUES (?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE assignments SET title = ?, description = ?, due_date = ?, course_id = ?, days_remaining = ? WHERE id = ?";
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, assignment.getTitle());
            pstmt.setString(2, assignment.getDescription());
            pstmt.setString(3, assignment.getDueDate());
            pstmt.setInt(4, assignment.getCourseId());
            pstmt.setInt(5, assignment.getDaysRemaining());

            if (assignment.getId() != 0) {
                pstmt.setInt(6, assignment.getId());
            }

            pstmt.executeUpdate();

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
        String sql = "SELECT id, title, description, due_date, course_id, days_remaining FROM assignments";

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
        String sql = "SELECT id, title, description, due_date, course_id, days_remaining FROM assignments WHERE id = ?";
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
        String sql = "SELECT id, title, description, due_date, course_id, days_remaining FROM assignments WHERE course_id = ?";

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

    private Assignment mapResultSetToAssignment(ResultSet rs) throws SQLException {
        Assignment assignment = new Assignment();
        assignment.setId(rs.getInt("id"));
        assignment.setTitle(rs.getString("title"));
        assignment.setDescription(rs.getString("description"));
        assignment.setDueDate(rs.getString("due_date"));
        assignment.setCourseId(rs.getInt("course_id"));
        assignment.setDaysRemaining(rs.getInt("days_remaining"));
        return assignment;
    }
}

