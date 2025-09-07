package com.servicedesk.service.impl;

import com.servicedesk.api.dto.comment.CommentCreateRequest;
import com.servicedesk.api.dto.comment.CommentResponse;
import com.servicedesk.api.dto.comment.CommentUpdateRequest;
import com.servicedesk.common.error.ConflictException;
import com.servicedesk.common.error.InvalidOrganizationException;
import com.servicedesk.common.error.ResourceNotFoundException;
import com.servicedesk.domain.model.Comment;
import com.servicedesk.domain.repositories.CommentRepository;
import com.servicedesk.domain.repositories.UserRepository;
import com.servicedesk.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<CommentResponse> getComments(UUID orgId, UUID issueId, Pageable pageable) {
        final Page<Comment> allComments = commentRepository
                .findByOrganizationIdAndIssueId(orgId, issueId, pageable);

        return allComments.map(CommentResponse::fromEntity);
    }

    @Override
    public CommentResponse getCommentById(UUID orgId, UUID commentId) {
        Comment comment = commentRepository.findByIdAndOrganizationId(commentId, orgId)
                .orElseThrow(() -> new ConflictException("Comment not found in the current organization " + commentId));

        return CommentResponse.fromEntity(comment);
    }

    @Override
    public CommentResponse createComment(UUID orgId, UUID issueId, CommentCreateRequest commentCreateRequest) {
        UUID authorId = UUID.fromString(commentCreateRequest.getAuthorId());
        userRepository.findByIdAndOrganizationId(authorId, orgId)
                .orElseThrow(() -> new InvalidOrganizationException("User doesn't have permission to create this comment"));
        Comment newComment = new Comment();
        newComment.setBody(commentCreateRequest.getBody());
        newComment.setAuthorId(UUID.fromString(commentCreateRequest.getAuthorId()));
        newComment.setIssueId(issueId);
        newComment.setOrganizationId(orgId);

        Comment savedComment = commentRepository.save(newComment);
        return CommentResponse.fromEntity(savedComment);
    }

    @Override
    public CommentResponse updateComment(UUID orgId, UUID commentId, CommentUpdateRequest commentUpdateRequest) {
        UUID authorId = UUID.fromString(commentUpdateRequest.getAuthorId());
        userRepository.findByIdAndOrganizationId(authorId, orgId)
                .orElseThrow(() -> new InvalidOrganizationException("User doesn't have permission to update this comment"));
        Comment existingComment = commentRepository.findByIdAndOrganizationId(commentId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found in the current organization"));
        existingComment.setBody(commentUpdateRequest.getBody());

        Comment comment = commentRepository.save(existingComment);
        return CommentResponse.fromEntity(comment);
    }

    @Override
    public void deleteComment(UUID orgId, UUID commentId) {
        Comment existingComment = commentRepository.findByIdAndOrganizationId(orgId, commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found in the current organization"));
        commentRepository.delete(existingComment);
    }
}
