package com.hams.doctor.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hams.doctor.exception.DownstreamServiceException;
import com.hams.doctor.exception.DuplicateResourceException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class AuthServiceErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        String errorCode = null;

        if (response.body() != null) {
            try (var inputStream = response.body().asInputStream()) {
                JsonNode body = objectMapper.readTree(inputStream);
                errorCode = body.path("code").asText(null);
            } catch (IOException exception) {
                // Use the generic downstream exception if parsing fails.
            }
        }

        if ("USERNAME_ALREADY_EXISTS".equals(errorCode)) {
            return new DuplicateResourceException(
                    "Username already exists");
        }

        return new DownstreamServiceException(
                "Auth Service request failed");
    }
}