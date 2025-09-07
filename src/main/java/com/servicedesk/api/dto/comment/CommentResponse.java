package com.servicedesk.api.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.servicedesk.domain.model.Comment;

import java.time.Instant;

public class CommentResponse {

    private String id;
    private String issueId;
    private String authorId;
    private String body;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public CommentResponse(String id, String issueId, String authorId, String body, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.issueId = issueId;
        this.authorId = authorId;
        this.body = body;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CommentResponse fromEntity(Comment comment) {
        return new CommentResponse(
                String.valueOf(comment.getId()),
                String.valueOf(comment.getIssueId()),
                String.valueOf(comment.getAuthorId()),
                comment.getBody(),
                comment.getCreatedAt(),
                comment.getUpdatedAt());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
