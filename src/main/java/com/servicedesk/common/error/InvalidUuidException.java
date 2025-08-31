package com.servicedesk.common.error;

public class InvalidUuidException extends RuntimeException {
    private final String field;

    public InvalidUuidException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() { return field; }
}
