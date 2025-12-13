package org.example.studentmanagement.entity;

public class AssignmentDetails {
    private int id;
    private int assignmentId;
    private int studentCourseDetailsId;
    private int isDone; // 0 = incomplete, 1 = completed

    public AssignmentDetails() {
    }

    public AssignmentDetails(int id, int assignmentId, int studentCourseDetailsId, int isDone) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.studentCourseDetailsId = studentCourseDetailsId;
        this.isDone = isDone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getStudentCourseDetailsId() {
        return studentCourseDetailsId;
    }

    public void setStudentCourseDetailsId(int studentCourseDetailsId) {
        this.studentCourseDetailsId = studentCourseDetailsId;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    @Override
    public boolean equals(Object comparedObject) {
        if (this == comparedObject) {
            return true;
        }

        if (!(comparedObject instanceof AssignmentDetails)) {
            return false;
        }

        AssignmentDetails compared = (AssignmentDetails) comparedObject;
        return this.id == compared.id;
    }
}

