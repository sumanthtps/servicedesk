package com.servicedesk.common.error;

public class InvalidOrganizationException extends RuntimeException {
    public InvalidOrganizationException(String message) {
        super(message);
    }
}
