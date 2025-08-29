package com.servicedesk.domain.model;

import com.servicedesk.common.enums.Priority;
import com.servicedesk.common.enums.Status;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "issues",
        indexes = {
                @Index(name = "idx_issues_project_status", columnList = "project_id,status,priority,created_at"),
                @Index(name = "idx_issues_assignee_status", columnList = "assignee_id,status"),
                @Index(name = "idx_issues_title_lower", columnList = "title") // functional idx is in SQL migration
        })
public class Issue extends Audited {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(name = "assignee_id")
    private UUID assigneeId;

    @Column(name = "title", nullable = false, length = 140)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 10)
    private Priority priority;

    protected Issue() {}

    public Issue(UUID id, UUID organizationId, UUID projectId, String title, Priority priority) {
        this.id = id;
        this.organizationId = organizationId;
        this.projectId = projectId;
        this.title = title;
        this.priority = priority;
        this.status = Status.OPEN;
    }

    public UUID getId() { return id; }
    public UUID getOrganizationId() { return organizationId; }
    public UUID getProjectId() { return projectId; }
    public UUID getAssigneeId() { return assigneeId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
    public Priority getPriority() { return priority; }

    public void setAssigneeId(UUID assigneeId) { this.assigneeId = assigneeId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(Status status) { this.status = status; }
    public void setPriority(Priority priority) { this.priority = priority; }
}
