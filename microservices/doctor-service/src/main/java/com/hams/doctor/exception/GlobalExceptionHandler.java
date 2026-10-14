package com.hams.doctor.exception;
import com.hams.doctor.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateResource(
            DuplicateResourceException exception
    ) {
         ApiErrorResponse response = new ApiErrorResponse(
        "USERNAME_ALREADY_EXISTS",
        "Username already exists",
        HttpStatus.CONFLICT.value(),
        Instant.now()
    );

    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(response);
    }

    @ExceptionHandler(DownstreamServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleDownstreamService(
            DownstreamServiceException exception
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
        "DOWNSTREAM_SERVICE_ERROR",
        "A dependent service request failed",
        HttpStatus.BAD_GATEWAY.value(),
        Instant.now()
    );

    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
        .body(response);
    }       
}