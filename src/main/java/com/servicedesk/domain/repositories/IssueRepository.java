package com.servicedesk.domain.repositories;

import com.servicedesk.domain.model.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IssueRepository extends JpaRepository<Issue, UUID> {
    Page<Issue> findAll(Specification<Issue> spec, Pageable pageable);

    Optional<Issue> findByIdAndOrganizationId(UUID id, UUID organizationId);
}
