package com.servicedesk.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "projects",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_projects_org_name", columnNames = {"organization_id","name"}),
                @UniqueConstraint(name = "uk_projects_org_key",  columnNames = {"organization_id","key"})
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

    protected Project() {}

    public Project(UUID id, UUID organizationId, String name, String key, String description) {
        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.key = key;
        this.description = description;
    }

    public UUID getId() { return id; }
    public UUID getOrganizationId() { return organizationId; }
    public String getName() { return name; }
    public String getKey() { return key; }
    public String getDescription() { return description; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
}
