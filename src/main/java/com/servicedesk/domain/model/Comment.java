package com.servicedesk.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "comments",
        indexes = {
                @Index(name = "idx_comments_issue_created", columnList = "issue_id,created_at")
        })
public class Comment extends Audited {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "issue_id", nullable = false)
    private UUID issueId;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @Column(name = "body", nullable = false, columnDefinition = "text")
    private String body; // 1..10000 enforced in DTO

    protected Comment() {}

    public Comment(UUID id, UUID organizationId, UUID issueId, UUID authorId, String body) {
        this.id = id;
        this.organizationId = organizationId;
        this.issueId = issueId;
        this.authorId = authorId;
        this.body = body;
    }

    public UUID getId() { return id; }
    public UUID getOrganizationId() { return organizationId; }
    public UUID getIssueId() { return issueId; }
    public UUID getAuthorId() { return authorId; }
    public String getBody() { return body; }

    public void setBody(String body) { this.body = body; }
}
