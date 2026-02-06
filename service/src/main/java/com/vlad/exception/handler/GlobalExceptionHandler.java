package com.vlad.exception.handler;

import com.vlad.exception.api.ApiException;
import com.vlad.exception.dto.ErrorResponse;
import com.vlad.exception.error.ErrorCode;
import com.vlad.exception.ex.RateLimitExceededException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitExceeded(RateLimitExceededException ex, HttpServletRequest request) {
        log.warn("Rate limit exceeded: {} from IP: {}", ex.getMessage(), getClientIP(request));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.RATE_LIMIT_EXCEEDED.name())
                .message(ex.getMessage())
                .status(HttpStatus.TOO_MANY_REQUESTS.value())
                .path(request.getRequestURI())
                .timestamp(OffsetDateTime.now())
                .details(Map.of(
                        "retryAfterSeconds", ex.getBlockTimeRemaining(),
                        "retryAfterMinutes", ex.getBlockTimeRemaining() / 60
                ))
                .build();

        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .header("Retry-After", String.valueOf(ex.getBlockTimeRemaining()))
                .body(errorResponse);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        HttpStatus status = mapErrorCodeToHttpStatus(ex.getErrorCode());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .code(ex.getErrorCode().name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .details(ex.getDetails())
                .build();
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .filter(fieldError -> fieldError.getDefaultMessage() != null)
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing
                ));

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .code(ErrorCode.VALIDATION_FAILED.name())
                .message(ErrorCode.VALIDATION_FAILED.getMessage())
                .path(request.getRequestURI())
                .details(fieldErrors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error occurred", ex);

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .code(ErrorCode.INTERNAL_ERROR.name())
                .message(ErrorCode.INTERNAL_ERROR.getMessage())
                .path(request.getRequestURI())
                .details(Map.of("exception", ex.getClass().getSimpleName(), "message", ex.getMessage()))
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private HttpStatus mapErrorCodeToHttpStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case VALIDATION_FAILED, TRIP_STATUS_INVALID -> HttpStatus.BAD_REQUEST;

            case INVALID_CREDENTIALS, INVALID_TOKEN -> HttpStatus.UNAUTHORIZED;

            case ACCESS_DENIED -> HttpStatus.FORBIDDEN;

            case RESOURCE_NOT_FOUND, VEHICLE_NOT_FOUND, DRIVER_NOT_FOUND, TRIP_NOT_FOUND,
                 REQUEST_NOT_FOUND, USER_NOT_FOUND -> HttpStatus.NOT_FOUND;

            case VEHICLE_ALREADY_EXISTS, DRIVER_ALREADY_EXISTS, TRIP_ALREADY_EXISTS, REQUEST_ALREADY_EXISTS,
                 USER_ALREADY_EXISTS -> HttpStatus.CONFLICT;

            case RATE_LIMIT_EXCEEDED -> HttpStatus.TOO_MANY_REQUESTS;

            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
