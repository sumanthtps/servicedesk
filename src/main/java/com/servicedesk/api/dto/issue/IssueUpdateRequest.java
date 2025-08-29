package com.servicedesk.api.dto.issue;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class IssueUpdateRequest {

    @Size(max = 140, message = "title must be at most 140 characters")
    private String title;

    @Size(max = 10000, message = "description must be at most 10000 characters")
    private String description;

    private String assigneeId; // optional, validate UUID if present

    @Pattern(regexp = "LOW|MEDIUM|HIGH", message = "priority must be LOW, MEDIUM, or HIGH")
    private String priority;

    @Pattern(regexp = "OPEN|IN_PROGRESS|RESOLVED|CLOSED", message = "status must be OPEN, IN_PROGRESS, RESOLVED, or CLOSED")
    private String status;

    public @Size(max = 140, message = "title must be at most 140 characters") String getTitle() {
        return title;
    }

    public void setTitle(@Size(max = 140, message = "title must be at most 140 characters") String title) {
        this.title = title;
    }

    public @Size(max = 10000, message = "description must be at most 10000 characters") String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 10000, message = "description must be at most 10000 characters") String description) {
        this.description = description;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public @Pattern(regexp = "LOW|MEDIUM|HIGH", message = "priority must be LOW, MEDIUM, or HIGH") String getPriority() {
        return priority;
    }

    public void setPriority(@Pattern(regexp = "LOW|MEDIUM|HIGH", message = "priority must be LOW, MEDIUM, or HIGH") String priority) {
        this.priority = priority;
    }

    public @Pattern(regexp = "OPEN|IN_PROGRESS|RESOLVED|CLOSED", message = "status must be OPEN, IN_PROGRESS, RESOLVED, or CLOSED") String getStatus() {
        return status;
    }

    public void setStatus(@Pattern(regexp = "OPEN|IN_PROGRESS|RESOLVED|CLOSED", message = "status must be OPEN, IN_PROGRESS, RESOLVED, or CLOSED") String status) {
        this.status = status;
    }
}
