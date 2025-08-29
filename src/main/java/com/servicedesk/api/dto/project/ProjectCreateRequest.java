package com.servicedesk.api.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProjectCreateRequest {

    @NotBlank(message = "name is required")
    @Size(max = 100, message = "name must be at most 100 characters")
    private String name;

    @NotBlank(message = "key is required")
    @Pattern(regexp = "^[A-Z]{2,10}$", message = "key must be 2–10 uppercase letters")
    private String key;

    @Size(max = 10000, message = "description must be at most 10000 characters")
    private String description;

    public @NotBlank(message = "name is required") @Size(max = 100, message = "name must be at most 100 characters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "name is required") @Size(max = 100, message = "name must be at most 100 characters") String name) {
        this.name = name;
    }

    public @NotBlank(message = "key is required") @Pattern(regexp = "^[A-Z]{2,10}$", message = "key must be 2–10 uppercase letters") String getKey() {
        return key;
    }

    public void setKey(@NotBlank(message = "key is required") @Pattern(regexp = "^[A-Z]{2,10}$", message = "key must be 2–10 uppercase letters") String key) {
        this.key = key;
    }

    public @Size(max = 10000, message = "description must be at most 10000 characters") String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 10000, message = "description must be at most 10000 characters") String description) {
        this.description = description;
    }
}
