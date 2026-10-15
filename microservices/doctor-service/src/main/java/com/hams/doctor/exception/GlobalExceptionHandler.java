package com.hams.doctor.exception;

import com.hams.doctor.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateResource(
            DuplicateResourceException exception) {
        ApiErrorResponse response = new ApiErrorResponse(
                "USERNAME_ALREADY_EXISTS",
                "Username already exists",
                HttpStatus.CONFLICT.value(),
                Instant.now());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(DownstreamServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleDownstreamService(
            DownstreamServiceException exception) {
        ApiErrorResponse response = new ApiErrorResponse(
                "DOWNSTREAM_SERVICE_ERROR",
                "A dependent service request failed",
                HttpStatus.BAD_GATEWAY.value(),
                Instant.now());

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRequestBody(
            HttpMessageNotReadableException exception) {
        ApiErrorResponse response = new ApiErrorResponse(
                "INVALID_REQUEST_BODY",
                "Request body is missing or contains invalid JSON",
                HttpStatus.BAD_REQUEST.value(),
                Instant.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException exception) {
        ApiErrorResponse response = new ApiErrorResponse(
                "VALIDATION_FAILED",
                "One or more request fields are invalid",
                HttpStatus.BAD_REQUEST.value(),
                Instant.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodValidationErrors(
            HandlerMethodValidationException exception) {
        ApiErrorResponse response = new ApiErrorResponse(
                "VALIDATION_FAILED",
                "One or more request parameters are invalid",
                HttpStatus.BAD_REQUEST.value(),
                Instant.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
            ResourceNotFoundException exception) {
        ApiErrorResponse response = new ApiErrorResponse(
                "RESOURCE_NOT_FOUND",
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                Instant.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidDoctorReviewStateException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidDoctorReviewState(
            InvalidDoctorReviewStateException exception) {
        ApiErrorResponse response = new ApiErrorResponse(
                "INVALID_DOCTOR_REVIEW_STATE",
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                Instant.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}