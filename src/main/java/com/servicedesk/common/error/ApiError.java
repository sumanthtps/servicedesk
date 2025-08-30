package com.servicedesk.common.error;

import java.time.Instant;
import java.util.List;

public class ApiError {

    private final String type;
    private final String title;
    private final int status;
    private final String detail;
    private final String instance;
    private final Instant timestamp;
    private final List<FieldError> errors;

    public ApiError(String type, String title, int status, String detail, String instance, List<FieldError> errors) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
        this.timestamp = Instant.now();
        this.errors = errors;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

    public String getInstance() {
        return instance;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    public static class FieldError {
        private final String field;
        private final String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }
}
