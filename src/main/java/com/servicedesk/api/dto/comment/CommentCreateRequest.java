package com.servicedesk.api.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequest {
    @NotBlank(message = "body is required")
    @Size(min = 1, max = 10000, message = "body must be 1–10000 characters")
    private String body;
}
