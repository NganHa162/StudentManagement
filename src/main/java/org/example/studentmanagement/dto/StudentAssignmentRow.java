package org.example.studentmanagement.dto;

import java.time.LocalDate;

public class StudentAssignmentRow {

    private final Long assignmentId;
    private final String title;
    private final String description;
    private final LocalDate dueDate;
    private final String courseName;
    private final boolean done;

    public StudentAssignmentRow(Long assignmentId, String title, String description, LocalDate dueDate,
            String courseName, boolean done) {
        this.assignmentId = assignmentId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.courseName = courseName;
        this.done = done;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public boolean isDone() {
        return done;
    }
}

