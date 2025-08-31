package com.servicedesk.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.UUID;

@Entity
@Table(name = "projects",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_projects_org_name", columnNames = {"organization_id", "name"}),
                @UniqueConstraint(name = "uk_projects_org_key", columnNames = {"organization_id", "key"})
        })
public class Project extends Audited {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // server-side validation via DTO enforces ^[A-Z]{2,10}$; DB length is 10
    @Column(name = "key", nullable = false, length = 10, updatable = false)
    private String key;

    @Column(name = "description", columnDefinition = "text")
    private String description; // ≤ 10000 by contract (enforced in DTO)

    public Project() {
    }

    public Project(UUID id, UUID organizationId, String name, String key, String description) {
        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.key = key;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
