package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentDAOImpl extends BaseDAOImpl<Student, Integer> implements StudentDAO{
    
    private final DataSource dataSource;

    @Autowired
    public StudentDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Student student) {
        String sql;
        if (student.getId() == 0) {
            // Insert new student
            sql = "INSERT INTO students (username, password, first_name, last_name, email) VALUES (?, ?, ?, ?, ?)";
        } else {
            // Update existing student
            sql = "UPDATE students SET username = ?, password = ?, first_name = ?, last_name = ?, email = ? WHERE id = ?";
        }
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, student.getUserName());
            pstmt.setString(2, student.getPassword());
            pstmt.setString(3, student.getFirstName());
            pstmt.setString(4, student.getLastName());
            pstmt.setString(5, student.getEmail());
            
            if (student.getId() != 0) {
                pstmt.setInt(6, student.getId());
            }
            
            pstmt.executeUpdate();
            
            // If it's a new student, get the generated ID
            if (student.getId() == 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        student.setId(rs.getInt(1));
                    }
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error saving student: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT id, username, password, first_name, last_name, email FROM students";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setUserName(rs.getString("username"));
                student.setPassword(rs.getString("password"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setEmail(rs.getString("email"));
                // Note: Courses will need to be loaded separately if needed
                students.add(student);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all students: " + e.getMessage(), e);
        }
        
        return students;
    }

    @Override
    public Student findById(Integer id) {
        String sql = "SELECT id, username, password, first_name, last_name, email FROM students WHERE id = ?";
        Student student = null;
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setUserName(rs.getString("username"));
                    student.setPassword(rs.getString("password"));
                    student.setFirstName(rs.getString("first_name"));
                    student.setLastName(rs.getString("last_name"));
                    student.setEmail(rs.getString("email"));
                    // Note: Courses will need to be loaded separately if needed
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding student by id: " + e.getMessage(), e);
        }
        
        return student;
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM students WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting student by id: " + e.getMessage(), e);
        }
    }
}
