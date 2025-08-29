package com.servicedesk.domain.repositories;

import com.servicedesk.domain.model.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRepository  extends JpaRepository<Organization, UUID> {
    Page<Organization> findByName(String name, Pageable pageable);
}
