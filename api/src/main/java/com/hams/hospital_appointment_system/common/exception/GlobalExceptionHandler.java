package com.hams.hospital_appointment_system.common.exception;

import com.hams.hospital_appointment_system.common.response.ErrorResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .message("An unexpected error occurred. Please try again later.")
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(errorResponse);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleBadCredentials(
                        BadCredentialsException ex) {

                ErrorResponse response = ErrorResponse.builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Invalid username or password.")
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(response);
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .message(exception.getMessage())
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(errorResponse);
        }

        @ExceptionHandler(DuplicateResourceException.class)
        public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException exception) {
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.CONFLICT.value())
                                .message(exception.getMessage())
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(errorResponse);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {

                Map<String, String> errors = exception.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .collect(Collectors.toMap(
                                                FieldError::getField,
                                                FieldError::getDefaultMessage,
                                                (existing, replacement) -> existing));

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Validation failed")
                                .errors(errors)
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(errorResponse);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleInvalidRequestBody(HttpMessageNotReadableException exception) {

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Invalid request body. Please check the data you provided")
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(errorResponse);
        }

        @ExceptionHandler(AppointmentSlotAlreadyBookedException.class)
        public ResponseEntity<ErrorResponse> handleAppointmentSlotAlreadyBookedException(
                        AppointmentSlotAlreadyBookedException exception) {
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.CONFLICT.value())
                                .message(exception.getMessage())
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(errorResponse);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
                        DataIntegrityViolationException exception) {

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.CONFLICT.value())
                                .message("Unable to process the request due to a data constraint.")
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(errorResponse);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAccessDenied(
                        AccessDeniedException exception) {

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.FORBIDDEN.value())
                                .message("You do not have permission to perform this action.")
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body(errorResponse);
        }

        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<ErrorResponse> handleMethodNotSupported(
                        HttpRequestMethodNotSupportedException exception) {

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                                .message("HTTP method '" + exception.getMethod()
                                                + "' is not supported for this endpoint.")
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity
                                .status(HttpStatus.METHOD_NOT_ALLOWED)
                                .body(errorResponse);
        }

        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<ErrorResponse> handleNoResourceFound(
                        NoResourceFoundException exception) {

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .message("The requested resource or endpoint was not found.")
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(errorResponse);
        }
}