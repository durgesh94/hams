
package com.hams.doctor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hams.doctor.service.ServiceTokenProvider;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class AuthFeignConfig {

    @Bean
    public ObjectMapper authObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ErrorDecoder authServiceErrorDecoder(
            ObjectMapper objectMapper) {
        return new AuthServiceErrorDecoder(objectMapper);
    }

    @Bean
    public RequestInterceptor feignAuthInterceptor(
            ServiceTokenProvider serviceTokenProvider) {
        return new FeignAuthInterceptor(serviceTokenProvider);
    }
}
