package com.servicedesk.api.dto.user;

import jakarta.validation.constraints.Size;

public class UserUpdateRequest {

    @Size(max = 100, message = "displayName must be at most 100 characters")
    private String displayName;

    private Boolean active;

    public @Size(max = 100, message = "displayName must be at most 100 characters") String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(@Size(max = 100, message = "displayName must be at most 100 characters") String displayName) {
        this.displayName = displayName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
