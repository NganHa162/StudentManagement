package org.example.studentmanagement.entity;

public class StudentCourseDetail {
    private int id;
    private int studentId;
    private int courseId;
    private String enrollmentDate;
    private String status; // e.g., "enrolled", "completed", "dropped"

    public StudentCourseDetail() {
    }

    public StudentCourseDetail(int id, int studentId, int courseId, String enrollmentDate, String status) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object comparedObject) {
        if (this == comparedObject) {
            return true;
        }

        if (!(comparedObject instanceof StudentCourseDetail)) {
            return false;
        }

        StudentCourseDetail compared = (StudentCourseDetail) comparedObject;
        return this.id == compared.id;
    }
}

