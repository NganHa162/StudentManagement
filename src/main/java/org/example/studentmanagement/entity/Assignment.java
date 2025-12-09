package org.example.studentmanagement.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Assignment {
    private int id;
    
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String title;
    
    private String description;
    
    @NotNull(message = "is required")
    private String dueDate;
    
    private int courseId;
    private int daysRemaining;

    public Assignment() {
    }

    public Assignment(int id, String title, String description, String dueDate, int courseId, int daysRemaining) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.courseId = courseId;
        this.daysRemaining = daysRemaining;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
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

        if (!(comparedObject instanceof Assignment compared)) {
            return false;
        }

        return this.id == compared.id;
    }
}

