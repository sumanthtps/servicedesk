package com.servicedesk.domain.repositories;

import com.servicedesk.domain.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findByOrganizationIdAndIssueIdOrderByCreatedAtAsc(UUID orgId, UUID issueId, Pageable page);
}
