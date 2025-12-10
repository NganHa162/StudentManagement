package org.example.studentmanagement.entity;

import java.util.List;

public class StudentCourseDetails {
    private int id;
    private int studentId;
    private int courseId;
    private GradeDetails gradeDetails;
    private List<Assignment> assignments;

    public StudentCourseDetails() {
    }

    public StudentCourseDetails(int id, int studentId, int courseId, GradeDetails gradeDetails, List<Assignment> assignments) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.gradeDetails = gradeDetails;
        this.assignments = assignments;
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

    public GradeDetails getGradeDetails() {
        return gradeDetails;
    }

    public void setGradeDetails(GradeDetails gradeDetails) {
        this.gradeDetails = gradeDetails;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public Assignment getAssignmentById(int assignmentId) {
        if (assignments != null) {
            for (Assignment assignment : assignments) {
                if (assignment.getId() == assignmentId) {
                    return assignment;
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object comparedObject) {
        if (this == comparedObject) {
            return true;
        }

        if (!(comparedObject instanceof StudentCourseDetails)) {
            return false;
        }

        StudentCourseDetails compared = (StudentCourseDetails) comparedObject;
        return this.id == compared.id;
    }
}

