package com.hams.doctor.config;

import com.hams.doctor.service.ServiceTokenProvider;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignAuthInterceptor implements RequestInterceptor {

    private final ServiceTokenProvider serviceTokenProvider;

    @Override
    public void apply(RequestTemplate template) {

        String token = serviceTokenProvider.getToken();

        template.header(
                "Authorization",
                "Bearer " + token
        );
    }
}