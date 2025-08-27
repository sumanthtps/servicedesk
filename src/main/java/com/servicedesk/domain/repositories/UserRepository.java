package com.servicedesk.domain.repositories;

import com.servicedesk.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository  extends JpaRepository<User, UUID> {
    Page<User> findByOrganizationId(UUID orgId, Pageable page);
    boolean existsByOrganizationIdAndUsername(UUID orgId, String username);
    boolean existsByOrganizationIdAndEmail(UUID orgId, String email);
}
