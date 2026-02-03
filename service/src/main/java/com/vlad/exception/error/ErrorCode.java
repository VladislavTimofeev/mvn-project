package com.vlad.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Validation failed"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested resource not found"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied"),

    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "User with this username already exists"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid username or password"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid or expired token"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),

    VEHICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "Vehicle not found"),
    VEHICLE_ALREADY_EXISTS(HttpStatus.CONFLICT, "Vehicle already exists"),

    DRIVER_NOT_FOUND(HttpStatus.NOT_FOUND, "Driver not found"),
    DRIVER_ALREADY_EXISTS(HttpStatus.CONFLICT, "Driver already exists"),

    TRIP_NOT_FOUND(HttpStatus.NOT_FOUND, "Trip not found"),
    TRIP_ALREADY_EXISTS(HttpStatus.CONFLICT, "Trip already exists"),
    TRIP_STATUS_INVALID(HttpStatus.BAD_REQUEST, "Trip status does not allow this operation"),

    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Request not found"),
    REQUEST_ALREADY_EXISTS(HttpStatus.CONFLICT, "Request already exists");

    private final HttpStatus httpStatus;
    private final String message;

    public String getCode() {
        return name();
    }
}
