package com.servicedesk.api.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CommentCreateRequest {

    @NotBlank(message = "body is required")
    @Size(min = 1, max = 10000, message = "body must be 1–10000 characters")
    private String body;

    // if you haven't added auth yet and want to accept authorId from client:
    @Pattern(regexp = "^[0-9a-fA-F-]{36}$", message = "authorId must be a UUID")
    @NotBlank(message = "authorId is required")
    private String authorId; // optional: must be UUID if present

    private String issueId;

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public @NotBlank(message = "body is required") @Size(min = 1, max = 10000, message = "body must be 1–10000 characters") String getBody() {
        return body;
    }

    public void setBody(@NotBlank(message = "body is required") @Size(min = 1, max = 10000, message = "body must be 1–10000 characters") String body) {
        this.body = body;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}
