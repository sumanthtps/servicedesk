package com.servicedesk.service.impl;

import com.servicedesk.api.dto.comment.CommentCreateRequest;
import com.servicedesk.api.dto.comment.CommentResponse;
import com.servicedesk.api.dto.comment.CommentUpdateRequest;
import com.servicedesk.common.error.InvalidOrganizationException;
import com.servicedesk.common.error.ResourceNotFoundException;
import com.servicedesk.domain.model.Comment;
import com.servicedesk.domain.repositories.CommentRepository;
import com.servicedesk.domain.repositories.IssueRepository;
import com.servicedesk.domain.repositories.UserRepository;
import com.servicedesk.security.JwtUser;
import com.servicedesk.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, IssueRepository issueRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.issueRepository = issueRepository;
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
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found in the current organization " + commentId));

        return CommentResponse.fromEntity(comment);
    }

    @Override
    public CommentResponse createComment(UUID orgId, UUID issueId, CommentCreateRequest commentCreateRequest) {
        if (issueRepository.findByIdAndOrganizationId(issueId, orgId)
                .isEmpty()) {
            throw new ResourceNotFoundException("Issue not found in this organization");
        }
        UUID authorId = currentUserId();
        userRepository.findByIdAndOrganizationId(authorId, orgId)
                .orElseThrow(() -> new InvalidOrganizationException("User doesn't have permission to create this comment"));
        Comment newComment = new Comment();
        newComment.setId(UUID.randomUUID());
        newComment.setBody(commentCreateRequest.getBody()
                .trim());
        newComment.setAuthorId(authorId);
        newComment.setIssueId(issueId);
        newComment.setOrganizationId(orgId);

        Comment savedComment = commentRepository.save(newComment);
        return CommentResponse.fromEntity(savedComment);
    }

    private UUID currentUserId() {
        Authentication a = SecurityContextHolder.getContext()
                .getAuthentication();
        if (a == null || !(a.getPrincipal() instanceof JwtUser u)) {
            throw new InvalidOrganizationException("Unauthenticated");
        }
        return u.getId();
    }

    @Override
    public CommentResponse updateComment(UUID orgId, UUID commentId, CommentUpdateRequest commentUpdateRequest) {
        UUID authorId = currentUserId();
        userRepository.findByIdAndOrganizationId(authorId, orgId)
                .orElseThrow(() -> new InvalidOrganizationException("User doesn't have permission to update this comment"));
        Comment existingComment = commentRepository.findByIdAndOrganizationId(commentId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found in the current organization"));
        if (!authorId.equals(existingComment.getAuthorId())) {
            throw new InvalidOrganizationException("You cannot edit this comment");
        }
        existingComment.setBody(commentUpdateRequest.getBody()
                .trim());

        Comment comment = commentRepository.save(existingComment);
        return CommentResponse.fromEntity(comment);
    }

    @Override
    @PreAuthorize("hasAuthority('ORG_ADMIN')")
    public void deleteComment(UUID orgId, UUID commentId) {
        Comment existingComment = commentRepository.findByIdAndOrganizationId(commentId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found in the current organization"));
        commentRepository.delete(existingComment);
    }
}
