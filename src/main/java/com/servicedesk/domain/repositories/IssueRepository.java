package com.servicedesk.domain.repositories;

import com.servicedesk.common.enums.Priority;
import com.servicedesk.common.enums.Status;
import com.servicedesk.domain.model.Issue;
import com.servicedesk.domain.model.Project;
import com.servicedesk.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Repository
public interface IssueRepository  extends JpaRepository<Issue, UUID> {
    Page<Issue> findAll(Specification<Issue> spec, Pageable pageable);
    Page<Issue> findByProjectId(Project project, Pageable pageable);
    Page<Issue> findByStatus(Status status, Pageable pageable);
    Page<Issue> findByPriority(Priority priority, Pageable pageable);
    Page<Issue> findByAssigneeId(User user, Pageable pageable);
    @Query("SELECT i FROM Issue i WHERE i.organizationId = :orgId AND LOWER(i.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Issue> searchByTitle(@Param("orgId") UUID orgId, @Param("title") String title, Pageable pageable);

}
