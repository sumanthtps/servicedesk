package com.servicedesk.controller;

import com.servicedesk.api.dto.comment.CommentCreateRequest;
import com.servicedesk.api.dto.comment.CommentResponse;
import com.servicedesk.api.dto.comment.CommentUpdateRequest;
import com.servicedesk.context.OrganizationContext;
import com.servicedesk.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping(path = "/issues/{issueId}/comments")
    public Page<CommentResponse> getCommentsByIssue(
            @PathVariable UUID issueId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int limit, // page size
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        UUID orgId = OrganizationContext.getOrgId();
        Sort sortBy = Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), sort);
        Pageable pageable = PageRequest.of(pageNumber, limit, sortBy);

        return commentService.getComments(orgId, issueId, pageable);
    }

    @GetMapping(path = "/comments/{id}")
    public CommentResponse getCommentById(@PathVariable UUID id) {
        UUID orgId = OrganizationContext.getOrgId();
        return commentService.getCommentById(orgId, id);
    }

    @PostMapping(path = "/issues/{issueId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable UUID issueId,
            @Valid @RequestBody CommentCreateRequest createRequest
    ) {
        UUID orgId = OrganizationContext.getOrgId();
        CommentResponse createdComment = commentService.createComment(orgId, issueId, createRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdComment.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(createdComment);
    }

    @PatchMapping(path = "/comments/{id}")
    public CommentResponse updateComment(@PathVariable UUID id, @Valid @RequestBody CommentUpdateRequest request) {
        UUID orgId = OrganizationContext.getOrgId();
        return commentService.updateComment(orgId, id, request);
    }

    @DeleteMapping(path = "/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID id) {
        UUID orgId = OrganizationContext.getOrgId();
        commentService.deleteComment(orgId, id);
        return ResponseEntity.noContent()
                .build();
    }


}
