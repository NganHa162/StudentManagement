package org.example.studentmanagement.entity;

public class GradeDetails {
    private int id;
    private int studentId;
    private int courseId;
    private String assignmentName;
    private double score;
    private double maxScore;
    private String grade; // e.g., "A", "B", "C", "D", "F"
    private String feedback;
    private String gradedDate;
    private int gradedByTeacherId;

    public GradeDetails() {
    }

    public GradeDetails(int id, int studentId, int courseId, String assignmentName, 
                       double score, double maxScore, String grade, String feedback, 
                       String gradedDate, int gradedByTeacherId) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.assignmentName = assignmentName;
        this.score = score;
        this.maxScore = maxScore;
        this.grade = grade;
        this.feedback = feedback;
        this.gradedDate = gradedDate;
        this.gradedByTeacherId = gradedByTeacherId;
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

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getGradedDate() {
        return gradedDate;
    }

    public void setGradedDate(String gradedDate) {
        this.gradedDate = gradedDate;
    }

    public int getGradedByTeacherId() {
        return gradedByTeacherId;
    }

    public void setGradedByTeacherId(int gradedByTeacherId) {
        this.gradedByTeacherId = gradedByTeacherId;
    }

    public double getPercentage() {
        if (maxScore > 0) {
            return (score / maxScore) * 100;
        }
        return 0.0;
    }

    @Override
    public boolean equals(Object comparedObject) {
        if (this == comparedObject) {
            return true;
        }

        if (!(comparedObject instanceof GradeDetails)) {
            return false;
        }

        GradeDetails compared = (GradeDetails) comparedObject;
        return this.id == compared.id;
    }
}

