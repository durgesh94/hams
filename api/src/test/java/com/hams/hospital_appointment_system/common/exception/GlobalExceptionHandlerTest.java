package com.hams.hospital_appointment_system.common.exception;

import com.hams.hospital_appointment_system.common.exception.DuplicateResourceException;
import com.hams.hospital_appointment_system.common.exception.GlobalExceptionHandler;
import com.hams.hospital_appointment_system.common.exception.ResourceNotFoundException;
import com.hams.hospital_appointment_system.common.response.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

        private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

        @Test
        void handleGenericException_shouldReturnInternalServerError() {

                ResponseEntity<ErrorResponse> response =
                                handler.handleGenericException(new RuntimeException("boom"));

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getStatus())
                                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
                assertThat(response.getBody().getMessage())
                                .isEqualTo("An unexpected error occurred. Please try again later.");
        }

        @Test
        void handleResourceNotFoundException_shouldReturnNotFound() {

                ResponseEntity<ErrorResponse> response =
                                handler.handleResourceNotFoundException(
                                                new ResourceNotFoundException("Patient not found with id 1"));

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getStatus())
                                .isEqualTo(HttpStatus.NOT_FOUND.value());
                assertThat(response.getBody().getMessage())
                                .isEqualTo("Patient not found with id 1");
        }

        @Test
        void handleDuplicateResourceException_shouldReturnConflict() {

                ResponseEntity<ErrorResponse> response =
                                handler.handleDuplicateResourceException(
                                                new DuplicateResourceException("Email already exists"));

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getStatus())
                                .isEqualTo(HttpStatus.CONFLICT.value());
                assertThat(response.getBody().getMessage())
                                .isEqualTo("Email already exists");
        }

        @Test
        void handleValidationException_shouldReturnBadRequestWithFieldErrors() {

                FieldError fieldError = new FieldError("patientRequest", "email", "Invalid email");

                BindingResult bindingResult = mock(BindingResult.class);
                when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

                MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
                when(exception.getBindingResult()).thenReturn(bindingResult);

                ResponseEntity<ErrorResponse> response = handler.handleValidationException(exception);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getStatus())
                                .isEqualTo(HttpStatus.BAD_REQUEST.value());
                assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
                assertThat(response.getBody().getErrors())
                                .containsEntry("email", "Invalid email");
        }

        @Test
        void handleInvalidRequestBody_shouldReturnBadRequest() {

                ResponseEntity<ErrorResponse> response =
                                handler.handleInvalidRequestBody(
                                                new HttpMessageNotReadableException("malformed json", (org.springframework.http.HttpInputMessage) null));

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getStatus())
                                .isEqualTo(HttpStatus.BAD_REQUEST.value());
                assertThat(response.getBody().getMessage())
                                .isEqualTo("Invalid request body. Please check the data you provided");
        }
}
