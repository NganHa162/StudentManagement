package org.example.studentmanagement.entity;

public class Assignment {
    private int id;
    private int courseId;
    private String title;
    private String description;
    private String dueDate;
    private double maxScore;
    private String createdDate;
    private String status; // e.g., "active", "closed", "draft"
    private int createdByTeacherId;
    private int daysRemaining; // calculated field for days until due date

    public Assignment() {
    }

    public Assignment(int id, int courseId, String title, String description, 
                    String dueDate, double maxScore, String createdDate, 
                    String status, int createdByTeacherId) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.maxScore = maxScore;
        this.createdDate = createdDate;
        this.status = status;
        this.createdByTeacherId = createdByTeacherId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCreatedByTeacherId() {
        return createdByTeacherId;
    }

    public void setCreatedByTeacherId(int createdByTeacherId) {
        this.createdByTeacherId = createdByTeacherId;
    }

    public boolean isActive() {
        return "active".equalsIgnoreCase(this.status);
    }

    public boolean isClosed() {
        return "closed".equalsIgnoreCase(this.status);
    }

    public int getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(int daysRemaining) {
        this.daysRemaining = daysRemaining;
    }

    @Override
    public boolean equals(Object comparedObject) {
        if (this == comparedObject) {
            return true;
        }

        if (!(comparedObject instanceof Assignment)) {
            return false;
        }

        Assignment compared = (Assignment) comparedObject;
        return this.id == compared.id;
    }
}

