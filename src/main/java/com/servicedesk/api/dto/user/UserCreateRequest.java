package com.servicedesk.api.dto.user;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserCreateRequest {

    @NotBlank(message = "username is required")
    @Size(min = 3, max = 50, message = "username must be 3–50 characters")
    private String username;

    @NotBlank(message = "displayName is required")
    @Size(max = 100, message = "displayName must be at most 100 characters")
    private String displayName;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    @Size(max = 255, message = "email must be at most 255 characters")
    private String email;

    // optional; default true in service if null
    private Boolean active;

    @PrePersist@PreUpdate
    public void updateEmail() {
        this.email = this.email.toLowerCase();
    }

    public @NotBlank(message = "username is required") @Size(min = 3, max = 50, message = "username must be 3–50 characters") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "username is required") @Size(min = 3, max = 50, message = "username must be 3–50 characters") String username) {
        this.username = username;
    }

    public @NotBlank(message = "displayName is required") @Size(max = 100, message = "displayName must be at most 100 characters") String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(@NotBlank(message = "displayName is required") @Size(max = 100, message = "displayName must be at most 100 characters") String displayName) {
        this.displayName = displayName;
    }

    public @NotBlank(message = "email is required") @Email(message = "email must be valid") @Size(max = 255, message = "email must be at most 255 characters") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "email is required") @Email(message = "email must be valid") @Size(max = 255, message = "email must be at most 255 characters") String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
