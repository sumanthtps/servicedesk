package com.servicedesk.domain.repositories;

import com.servicedesk.domain.model.Organization;
import com.servicedesk.domain.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Repository
public interface ProjectRepository  extends JpaRepository<Project, UUID> {
    Page<Project> findByOrganizationId(Organization organization, Pageable page);
    boolean existsByOrganizationIdAndKey(UUID orgId, String key);
    boolean existsByOrganizationIdAndName(UUID orgId, String name);
}
