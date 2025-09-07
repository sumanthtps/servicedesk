package com.servicedesk.service;

import com.servicedesk.api.dto.comment.CommentCreateRequest;
import com.servicedesk.api.dto.comment.CommentResponse;
import com.servicedesk.api.dto.comment.CommentUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CommentService {
    Page<CommentResponse> getComments(UUID orgId, UUID issueId, Pageable pageable);

    CommentResponse getCommentById(UUID orgId, UUID commentId);

    CommentResponse createComment(UUID orgId, UUID issueId, CommentCreateRequest commentCreateRequest);

    CommentResponse updateComment(UUID orgId, UUID commentId, CommentUpdateRequest commentUpdateRequest);

    void deleteComment(UUID orgId, UUID commentId);
}
