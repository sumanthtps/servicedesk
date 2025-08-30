package com.servicedesk.common.web;

import com.servicedesk.common.error.ApiError;
import com.servicedesk.common.error.ApiError.FieldError;
import com.servicedesk.common.error.ConflictException;
import com.servicedesk.common.error.InvalidSortFieldException;
import com.servicedesk.common.error.InvalidUuidException;
import com.servicedesk.common.error.ResourceNotFoundException;
import com.servicedesk.context.OrganizationContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---- VALIDATION: @Valid on request body ----
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> onBodyValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new FieldError(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        return respond(req, HttpStatus.BAD_REQUEST,
                "https://api.servicedesk/errors/validation-error",
                "Validation failed",
                "One or more fields are invalid.",
                fieldErrors);
    }

    private ResponseEntity<ApiError> respond(HttpServletRequest req,
                                             HttpStatus status,
                                             String type,
                                             String title,
                                             String detail,
                                             List<FieldError> fields) {
        ApiError body = new ApiError(type, title, status.value(), detail, req.getRequestURI(), fields);
        return ResponseEntity.status(status)
                .body(body);
    }

    // ---- VALIDATION: query/path parameter constraints ----
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> onConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        List<FieldError> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(this::toFieldError)
                .collect(Collectors.toList());

        return respond(req, HttpStatus.BAD_REQUEST,
                "https://api.servicedesk/errors/validation-error",
                "Validation failed",
                "One or more parameters are invalid.",
                fieldErrors);
    }

    private FieldError toFieldError(ConstraintViolation<?> v) {
        // propertyPath like "listIssues.arg0.titleContains" or "create.projectCreateRequest.name"
        String field = v.getPropertyPath() != null ? v.getPropertyPath()
                .toString() : "param";
        // Try to keep only the last segment
        int dot = field.lastIndexOf('.');
        if (dot >= 0 && dot < field.length() - 1) {
            field = field.substring(dot + 1);
        }
        return new FieldError(field, v.getMessage());
    }

    // ---- Malformed JSON / wrong types in body ----
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> onUnreadableBody(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return respond(req, HttpStatus.BAD_REQUEST,
                "https://api.servicedesk/errors/bad-request",
                "Bad request",
                "Malformed JSON or wrong data types.",
                null);
    }

    // ---- Missing required header (e.g., X-Org-Id) ----
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiError> onMissingHeader(MissingRequestHeaderException ex, HttpServletRequest req) {
        List<FieldError> errors = List.of(new FieldError(ex.getHeaderName(), "header is required"));
        return respond(req, HttpStatus.BAD_REQUEST,
                "https://api.servicedesk/errors/bad-request",
                "Bad request",
                "Missing required header.",
                errors);
    }

    // ---- Path variable / request param type mismatch ----
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> onTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        boolean targetIsUuid = ex.getRequiredType() != null && ex.getRequiredType()
                .equals(UUID.class);
        String field = ex.getName();
        if (targetIsUuid) {
            List<FieldError> list = List.of(new FieldError(field, "must be a valid UUID"));
            return respond(req, HttpStatus.UNPROCESSABLE_ENTITY,
                    "https://api.servicedesk/errors/invalid-uuid",
                    "Invalid identifier",
                    "Invalid UUID format.",
                    list);
        }
        List<FieldError> list = List.of(new FieldError(field, "invalid value"));
        return respond(req, HttpStatus.BAD_REQUEST,
                "https://api.servicedesk/errors/bad-request",
                "Bad request",
                "Parameter has invalid type.",
                list);
    }

    // ---- Custom: invalid UUID (when you validate manually) ----
    @ExceptionHandler(InvalidUuidException.class)
    public ResponseEntity<ApiError> onInvalidUuid(InvalidUuidException ex, HttpServletRequest req) {
        List<FieldError> list = List.of(new FieldError(ex.getField(), ex.getMessage()));
        return respond(req, HttpStatus.UNPROCESSABLE_ENTITY,
                "https://api.servicedesk/errors/invalid-uuid",
                "Invalid identifier",
                "Invalid UUID format.",
                list);
    }

    // ---- 404: not found ----
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> onNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return respond(req, HttpStatus.NOT_FOUND,
                "https://api.servicedesk/errors/not-found",
                "Resource not found",
                ex.getMessage(),
                null);
    }

    // ---- 409: conflict (duplicates, unique constraint) ----
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> onConflict(ConflictException ex, HttpServletRequest req) {
        return respond(req, HttpStatus.CONFLICT,
                "https://api.servicedesk/errors/conflict",
                "Conflict",
                ex.getMessage(),
                null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> onDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        // Mask internal SQL details; return a generic conflict
        return respond(req, HttpStatus.CONFLICT,
                "https://api.servicedesk/errors/conflict",
                "Conflict",
                "Request conflicts with existing data.",
                null);
    }

    // ---- 400: invalid sort field ----
    @ExceptionHandler(InvalidSortFieldException.class)
    public ResponseEntity<ApiError> onInvalidSort(InvalidSortFieldException ex, HttpServletRequest req) {
        String allowed = String.join(", ", ex.getAllowed());
        String detail = "Sort by '" + ex.getBadField() + "' is not allowed. Allowed: " + allowed;
        List<FieldError> list = List.of(new FieldError("sort", ex.getBadField() + " is not allowed"));
        return respond(req, HttpStatus.BAD_REQUEST,
                "https://api.servicedesk/errors/invalid-sort",
                "Invalid sort field",
                detail,
                list);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> onMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        return respond(req, HttpStatus.METHOD_NOT_ALLOWED,
                "https://api.servicedesk/errors/method-not-allowed",
                "Method not allowed",
                ex.getMessage(),
                null);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> onUnsupportedMediaType(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        return respond(req, HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "https://api.servicedesk/errors/unsupported-media-type",
                "Unsupported media type",
                ex.getMessage(),
                null);
    }

    // ---- 500 fallback ----
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> onUnhandled(Exception ex, HttpServletRequest req) {
        // Log ex on server side; keep client detail generic
        return respond(req, HttpStatus.INTERNAL_SERVER_ERROR,
                "https://api.servicedesk/errors/internal",
                "Internal server error",
                "Unexpected error.",
                null);
    }

    @AfterThrowing("within(@org.springframework.web.bind.annotation.RestController *)")
    public void clearContextAfterException() {
        OrganizationContext.clear();
    }

    @AfterReturning("within(@org.springframework.web.bind.annotation.RestController *)")
    public void clearContextAfterSuccess() {
        OrganizationContext.clear();
    }
}
