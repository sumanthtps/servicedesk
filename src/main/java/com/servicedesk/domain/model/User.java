package com.servicedesk.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_org_username", columnNames = {"organization_id","username"}),
                @UniqueConstraint(name = "uk_users_org_email",    columnNames = {"organization_id","email"})
        })
public class User extends Audited {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "id", nullable = false, length = 50)
    private String username;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name="organization_id", nullable = false)
    private UUID organizationId;

    public User() {}

    public User(UUID id, String username, String displayName, String email, UUID organizationId) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.email = email == null ? null: email.toLowerCase();
        this.active = true;
        this.organizationId = organizationId;
    }

    @PrePersist @PreUpdate
    private void normalize() {
        if (this.email != null) this.email = this.email.toLowerCase();
    }

    public UUID getId() { return id; }
    public UUID getOrganizationId() { return organizationId; }
    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }
    public String getEmail() { return email; }
    public boolean isActive() { return active; }

    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setActive(boolean active) { this.active = active; }
}
