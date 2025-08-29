package com.servicedesk.api.dto.issue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class IssueCreateRequest {

    @NotBlank(message = "projectId is required")
    private String projectId; // validate UUID format in controller/service

    @NotBlank(message = "title is required")
    @Size(max = 140, message = "title must be at most 140 characters")
    private String title;

    @Size(max = 10000, message = "description must be at most 10000 characters")
    private String description;

    private String assigneeId; // optional, validate UUID if present

    @NotBlank(message = "priority is required")
    @Pattern(regexp = "LOW|MEDIUM|HIGH", message = "priority must be LOW, MEDIUM, or HIGH")
    private String priority;

    public @NotBlank(message = "projectId is required") String getProjectId() {
        return projectId;
    }

    public void setProjectId(@NotBlank(message = "projectId is required") String projectId) {
        this.projectId = projectId;
    }

    public @NotBlank(message = "title is required") @Size(max = 140, message = "title must be at most 140 characters") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "title is required") @Size(max = 140, message = "title must be at most 140 characters") String title) {
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

    public @NotBlank(message = "priority is required") @Pattern(regexp = "LOW|MEDIUM|HIGH", message = "priority must be LOW, MEDIUM, or HIGH") String getPriority() {
        return priority;
    }

    public void setPriority(@NotBlank(message = "priority is required") @Pattern(regexp = "LOW|MEDIUM|HIGH", message = "priority must be LOW, MEDIUM, or HIGH") String priority) {
        this.priority = priority;
    }
}
