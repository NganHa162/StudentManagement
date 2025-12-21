package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CourseDAOImpl extends BaseDAOImpl<Course, Integer> implements CourseDAO{
    
    private final DataSource dataSource;

    @Autowired
    public CourseDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Course course) {
        String sql;
        if (course.getId() == 0) {
            // Insert new course
            sql = "INSERT INTO courses (code, name, teacher_id) VALUES (?, ?, ?)";
        } else {
            // Update existing course
            sql = "UPDATE courses SET code = ?, name = ?, teacher_id = ? WHERE id = ?";
        }
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, course.getCode());
            pstmt.setString(2, course.getName());
            
            if (course.getTeacher() != null) {
                pstmt.setInt(3, course.getTeacher().getId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            
            if (course.getId() != 0) {
                pstmt.setInt(4, course.getId());
            }
            
            pstmt.executeUpdate();
            
            // If it's a new course, get the generated ID
            if (course.getId() == 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        course.setId(rs.getInt(1));
                    }
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error saving course: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Course> findAll(){
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT id, code, name, teacher_id FROM courses";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setCode(rs.getString("code"));
                course.setName(rs.getString("name"));
                // Note: Teacher and Students will need to be loaded separately if needed
                courses.add(course);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all courses: " + e.getMessage(), e);
        }
        
        return courses;
    }

    @Override
    public Course findById(Integer id){
        String sql = "SELECT id, code, name, teacher_id FROM courses WHERE id = ?";
        Course course = null;
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    course = new Course();
                    course.setId(rs.getInt("id"));
                    course.setCode(rs.getString("code"));
                    course.setName(rs.getString("name"));
                    // Note: Teacher and Students will need to be loaded separately if needed
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding course by id: " + e.getMessage(), e);
        }
        
        return course;
    }

    @Override
    public void deleteById(Integer id){
        String sql = "DELETE FROM courses WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting course by id: " + e.getMessage(), e);
        }
    }

    public Course findByCode(String code) {
        String sql = "SELECT id, code, name, teacher_id FROM courses WHERE code = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Course c = new Course();
                    c.setId(rs.getInt("id"));
                    c.setCode(rs.getString("code"));
                    c.setName(rs.getString("name"));
                    return c;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding course by code: " + e.getMessage(), e);
        }
    }

}
