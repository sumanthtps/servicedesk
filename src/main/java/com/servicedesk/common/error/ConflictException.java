package com.servicedesk.common.error;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
}
