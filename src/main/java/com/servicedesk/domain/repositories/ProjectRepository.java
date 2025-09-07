package com.servicedesk.domain.repositories;

import com.servicedesk.domain.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Page<Project> findByOrganizationId(UUID organizationId, Pageable page);

    boolean existsByOrganizationIdAndKey(UUID orgId, String key);

    boolean existsByIdAndOrganizationId(UUID orgId, UUID id);

    boolean existsByOrganizationIdAndNameAndIdNot(UUID orgId, String name, UUID id);

    boolean existsByOrganizationIdAndName(UUID orgId, String name);
}
