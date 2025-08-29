package com.servicedesk.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="organizations",
        uniqueConstraints = {
            @UniqueConstraint(name="uk_organizations_name", columnNames = "name")
})
public class Organization extends Audited {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name="name",  nullable = false, length = 100)
    private String name;

    public Organization() {}

    public Organization(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
