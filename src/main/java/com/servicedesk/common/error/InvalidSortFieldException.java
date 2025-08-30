package com.servicedesk.common.error;

import java.util.Set;

public class InvalidSortFieldException extends RuntimeException {
    private final String badField;
    private final Set<String> allowed;

    public InvalidSortFieldException(String badField, Set<String> allowed) {
        super("Invalid sort field: " + badField);
        this.badField = badField;
        this.allowed = allowed;
    }

    public String getBadField() { return badField; }
    public Set<String> getAllowed() { return allowed; }
}
