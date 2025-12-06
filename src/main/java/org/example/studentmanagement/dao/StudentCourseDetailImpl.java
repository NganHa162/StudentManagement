package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.StudentCourseDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentCourseDetailImpl extends BaseDAOImpl<StudentCourseDetail, Integer> implements StudentCourseDetailDAO {

    private final DataSource dataSource;

    @Autowired
    public StudentCourseDetailImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(StudentCourseDetail detail) {
        String sql;
        if (detail.getId() == 0) {
            // Insert new enrollment
            sql = "INSERT INTO student_course_detail (student_id, course_id, enrollment_date, status) VALUES (?, ?, ?, ?)";
        } else {
            // Update existing enrollment
            sql = "UPDATE student_course_detail SET student_id = ?, course_id = ?, enrollment_date = ?, status = ? WHERE id = ?";
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, detail.getStudentId());
            pstmt.setInt(2, detail.getCourseId());
            pstmt.setString(3, detail.getEnrollmentDate());
            pstmt.setString(4, detail.getStatus());

            if (detail.getId() != 0) {
                pstmt.setInt(5, detail.getId());
            }

            pstmt.executeUpdate();

            // If it's a new enrollment, get the generated ID
            if (detail.getId() == 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        detail.setId(rs.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving student course detail: " + e.getMessage(), e);
        }
    }

    @Override
    public List<StudentCourseDetail> findAll() {
        List<StudentCourseDetail> details = new ArrayList<>();
        String sql = "SELECT id, student_id, course_id, enrollment_date, status FROM student_course_detail";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                StudentCourseDetail detail = new StudentCourseDetail();
                detail.setId(rs.getInt("id"));
                detail.setStudentId(rs.getInt("student_id"));
                detail.setCourseId(rs.getInt("course_id"));
                detail.setEnrollmentDate(rs.getString("enrollment_date"));
                detail.setStatus(rs.getString("status"));
                details.add(detail);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all student course details: " + e.getMessage(), e);
        }

        return details;
    }

    @Override
    public StudentCourseDetail findById(Integer id) {
        String sql = "SELECT id, student_id, course_id, enrollment_date, status FROM student_course_detail WHERE id = ?";
        StudentCourseDetail detail = null;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    detail = new StudentCourseDetail();
                    detail.setId(rs.getInt("id"));
                    detail.setStudentId(rs.getInt("student_id"));
                    detail.setCourseId(rs.getInt("course_id"));
                    detail.setEnrollmentDate(rs.getString("enrollment_date"));
                    detail.setStatus(rs.getString("status"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding student course detail by id: " + e.getMessage(), e);
        }

        return detail;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM student_course_detail WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting student course detail by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<StudentCourseDetail> findByStudentId(int studentId) {
        List<StudentCourseDetail> details = new ArrayList<>();
        String sql = "SELECT id, student_id, course_id, enrollment_date, status FROM student_course_detail WHERE student_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    StudentCourseDetail detail = new StudentCourseDetail();
                    detail.setId(rs.getInt("id"));
                    detail.setStudentId(rs.getInt("student_id"));
                    detail.setCourseId(rs.getInt("course_id"));
                    detail.setEnrollmentDate(rs.getString("enrollment_date"));
                    detail.setStatus(rs.getString("status"));
                    details.add(detail);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding student course details by student id: " + e.getMessage(), e);
        }

        return details;
    }

    @Override
    public List<StudentCourseDetail> findByCourseId(int courseId) {
        List<StudentCourseDetail> details = new ArrayList<>();
        String sql = "SELECT id, student_id, course_id, enrollment_date, status FROM student_course_detail WHERE course_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    StudentCourseDetail detail = new StudentCourseDetail();
                    detail.setId(rs.getInt("id"));
                    detail.setStudentId(rs.getInt("student_id"));
                    detail.setCourseId(rs.getInt("course_id"));
                    detail.setEnrollmentDate(rs.getString("enrollment_date"));
                    detail.setStatus(rs.getString("status"));
                    details.add(detail);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding student course details by course id: " + e.getMessage(), e);
        }

        return details;
    }
}
