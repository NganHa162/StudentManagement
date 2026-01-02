package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.Course;
import org.example.studentmanagement.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TeacherDAOImpl extends BaseDAOImpl<Teacher, Integer> implements TeacherDAO{
    
    private final DataSource dataSource;

    @Autowired
    public TeacherDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Teacher teacher) {
        String sql;
        if (teacher.getId() == 0) {
            // Insert new teacher
            sql = "INSERT INTO teachers (username, password, first_name, last_name, email) VALUES (?, ?, ?, ?, ?)";
        } else {
            // Update existing teacher
            sql = "UPDATE teachers SET username = ?, password = ?, first_name = ?, last_name = ?, email = ? WHERE id = ?";
        }
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, teacher.getUserName());
            pstmt.setString(2, teacher.getPassword());
            pstmt.setString(3, teacher.getFirstName());
            pstmt.setString(4, teacher.getLastName());
            pstmt.setString(5, teacher.getEmail());
            
            if (teacher.getId() != 0) {
                pstmt.setInt(6, teacher.getId());
            }
            
            pstmt.executeUpdate();
            
            // If it's a new teacher, get the generated ID
            if (teacher.getId() == 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        teacher.setId(rs.getInt(1));
                    }
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error saving teacher: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Teacher> findAll() {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT id, username, password, first_name, last_name, email FROM teachers";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Teacher teacher = new Teacher();
                teacher.setId(rs.getInt("id"));
                teacher.setUserName(rs.getString("username"));
                teacher.setPassword(rs.getString("password"));
                teacher.setFirstName(rs.getString("first_name"));
                teacher.setLastName(rs.getString("last_name"));
                teacher.setEmail(rs.getString("email"));
                
                // Load courses
                List<Course> courses = new ArrayList<>();
                String courseSql = "SELECT id, code, name FROM courses WHERE teacher_id = ?";
                try (PreparedStatement coursePstmt = conn.prepareStatement(courseSql)) {
                    coursePstmt.setInt(1, teacher.getId());
                    try (ResultSet courseRs = coursePstmt.executeQuery()) {
                        while (courseRs.next()) {
                            Course course = new Course();
                            course.setId(courseRs.getInt("id"));
                            course.setCode(courseRs.getString("code"));
                            course.setName(courseRs.getString("name"));
                            courses.add(course);
                        }
                    }
                }
                teacher.setCourses(courses);
                
                teachers.add(teacher);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all teachers: " + e.getMessage(), e);
        }
        
        return teachers;
    }

    @Override
    public Teacher findById(Integer id) {
        String sql = "SELECT id, username, password, first_name, last_name, email FROM teachers WHERE id = ?";
        Teacher teacher = null;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    teacher = new Teacher();
                    teacher.setId(rs.getInt("id"));
                    teacher.setUserName(rs.getString("username"));
                    teacher.setPassword(rs.getString("password"));
                    teacher.setFirstName(rs.getString("first_name"));
                    teacher.setLastName(rs.getString("last_name"));
                    teacher.setEmail(rs.getString("email"));

                    // Load courses
                    List<Course> courses = new ArrayList<>();
                    String courseSql = "SELECT id, code, name FROM courses WHERE teacher_id = ?";
                    try (PreparedStatement coursePstmt = conn.prepareStatement(courseSql)) {
                        coursePstmt.setInt(1, teacher.getId());
                        try (ResultSet courseRs = coursePstmt.executeQuery()) {
                            while (courseRs.next()) {
                                Course course = new Course();
                                course.setId(courseRs.getInt("id"));
                                course.setCode(courseRs.getString("code"));
                                course.setName(courseRs.getString("name"));
                                courses.add(course);
                            }
                        }
                    }
                    teacher.setCourses(courses);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding teacher by id: " + e.getMessage(), e);
        }

        return teacher;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM teachers WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting teacher by id: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Teacher> findByUserName(String userName) {
        String sql = "SELECT id, username, password, first_name, last_name, email FROM teachers WHERE username = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Teacher teacher = new Teacher();
                    teacher.setId(rs.getInt("id"));
                    teacher.setUserName(rs.getString("username"));
                    teacher.setPassword(rs.getString("password"));
                    teacher.setFirstName(rs.getString("first_name"));
                    teacher.setLastName(rs.getString("last_name"));
                    teacher.setEmail(rs.getString("email"));

                    // Load courses
                    List<Course> courses = new ArrayList<>();
                    String courseSql = "SELECT id, code, name FROM courses WHERE teacher_id = ?";
                    try (PreparedStatement coursePstmt = conn.prepareStatement(courseSql)) {
                        coursePstmt.setInt(1, teacher.getId());
                        try (ResultSet courseRs = coursePstmt.executeQuery()) {
                            while (courseRs.next()) {
                                Course course = new Course();
                                course.setId(courseRs.getInt("id"));
                                course.setCode(courseRs.getString("code"));
                                course.setName(courseRs.getString("name"));
                                courses.add(course);
                            }
                        }
                    }
                    teacher.setCourses(courses);

                    return Optional.of(teacher);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding teacher by username: " + e.getMessage(), e);
        }

        return Optional.empty();
    }
}
