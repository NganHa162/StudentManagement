package org.example.studentmanagement.dao;

import org.example.studentmanagement.entity.Course;
import org.example.studentmanagement.entity.Student;
import org.example.studentmanagement.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
        String sql = "SELECT c.id, c.code, c.name, c.teacher_id, t.first_name AS firstName, t.last_name AS lastName FROM courses c LEFT JOIN teachers t ON c.teacher_id = t.id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setCode(rs.getString("code"));
                course.setName(rs.getString("name"));

                // Set teacher if exists
                int teacherId = rs.getInt("teacher_id");
                if (!rs.wasNull()) {
                    Teacher teacher = new Teacher();
                    teacher.setId(teacherId);
                    teacher.setFirstName(rs.getString("firstName"));
                    teacher.setLastName(rs.getString("lastName"));
                    course.setTeacher(teacher);
                }

                // Load students
                List<Student> students = new ArrayList<>();
                String studentSql = "SELECT s.id, s.first_name, s.last_name FROM students s JOIN student_course_details scd ON s.id = scd.student_id WHERE scd.course_id = ?";
                try (PreparedStatement studentPstmt = conn.prepareStatement(studentSql)) {
                    studentPstmt.setInt(1, course.getId());
                    try (ResultSet studentRs = studentPstmt.executeQuery()) {
                        while (studentRs.next()) {
                            Student student = new Student();
                            student.setId(studentRs.getInt("id"));
                            student.setFirstName(studentRs.getString("first_name"));
                            student.setLastName(studentRs.getString("last_name"));
                            students.add(student);
                        }
                    }
                }
                course.setStudents(students);

                courses.add(course);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all courses: " + e.getMessage(), e);
        }

        return courses;
    }

    @Override
    public Course findById(Integer id){
        String sql = "SELECT c.id, c.code, c.name, c.teacher_id, t.first_name AS firstName, t.last_name AS lastName FROM courses c LEFT JOIN teachers t ON c.teacher_id = t.id WHERE c.id = ?";
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

                    // Set teacher if exists
                    int teacherId = rs.getInt("teacher_id");
                    if (!rs.wasNull()) {
                        Teacher teacher = new Teacher();
                        teacher.setId(teacherId);
                        teacher.setFirstName(rs.getString("firstName"));
                        teacher.setLastName(rs.getString("lastName"));
                        course.setTeacher(teacher);
                    }

                    // Load students
                    List<Student> students = new ArrayList<>();
                    String studentSql = "SELECT s.id, s.first_name, s.last_name FROM students s JOIN student_course_details scd ON s.id = scd.student_id WHERE scd.course_id = ?";
                    try (PreparedStatement studentPstmt = conn.prepareStatement(studentSql)) {
                        studentPstmt.setInt(1, id);
                        try (ResultSet studentRs = studentPstmt.executeQuery()) {
                            while (studentRs.next()) {
                                Student student = new Student();
                                student.setId(studentRs.getInt("id"));
                                student.setFirstName(studentRs.getString("first_name"));
                                student.setLastName(studentRs.getString("last_name"));
                                students.add(student);
                            }
                        }
                    }
                    course.setStudents(students);
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
}
