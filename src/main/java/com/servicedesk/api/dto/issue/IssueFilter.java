package com.servicedesk.api.dto.issue;

import com.servicedesk.common.enums.Priority;
import com.servicedesk.common.enums.Status;

import java.util.UUID;

public class IssueFilter {
    private UUID projectId;
    private Status status;
    private Priority priority;
    private UUID assigneeId;
    private String titleContains;

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public UUID getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(UUID assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getTitleContains() {
        return titleContains;
    }

    public void setTitleContains(String titleContains) {
        this.titleContains = titleContains;
    }

    // getters & setters
}
