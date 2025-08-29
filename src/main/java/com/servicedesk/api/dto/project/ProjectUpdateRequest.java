package com.servicedesk.api.dto.project;

import jakarta.validation.constraints.Size;

public class ProjectUpdateRequest {

    @Size(max = 100, message = "name must be at most 100 characters")
    private String name;

    @Size(max = 10000, message = "description must be at most 10000 characters")
    private String description;

    public @Size(max = 100, message = "name must be at most 100 characters") String getName() {
        return name;
    }

    public void setName(@Size(max = 100, message = "name must be at most 100 characters") String name) {
        this.name = name;
    }

    public @Size(max = 10000, message = "description must be at most 10000 characters") String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 10000, message = "description must be at most 10000 characters") String description) {
        this.description = description;
    }
}
