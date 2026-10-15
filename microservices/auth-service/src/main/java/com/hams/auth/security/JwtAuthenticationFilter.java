package com.hams.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);

        try {

            String tokenType = jwtService.extractTokenType(jwt);

            if (SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                /*
                 * Service-to-service JWT
                 */
                if ("SERVICE".equals(tokenType)) {

                    if (jwtService.isServiceTokenValid(jwt)) {

                        String serviceName =
                                jwtService.extractUsername(jwt);

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        serviceName,
                                        null,
                                        Collections.emptyList()
                                );

                        authentication.setDetails(
                                new WebAuthenticationDetailsSource()
                                        .buildDetails(request)
                        );

                        SecurityContextHolder
                                .getContext()
                                .setAuthentication(authentication);

                        log.debug(
                                "Service JWT authentication successful: {}",
                                serviceName
                        );
                    }

                    /*
                     * Normal user JWT
                     */
                } else {

                    String username =
                            jwtService.extractUsername(jwt);

                    if (username != null) {

                        UserDetails userDetails =
                                userDetailsService
                                        .loadUserByUsername(username);

                        if (jwtService.isTokenValid(jwt, userDetails)) {

                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails,
                                            null,
                                            userDetails.getAuthorities()
                                    );

                            authentication.setDetails(
                                    new WebAuthenticationDetailsSource()
                                            .buildDetails(request)
                            );

                            SecurityContextHolder
                                    .getContext()
                                    .setAuthentication(authentication);

                            log.debug(
                                    "JWT authentication successful for user: {}",
                                    username
                            );
                        }
                    }
                }
            }

        } catch (Exception exception) {

            log.warn(
                    "JWT VALIDATION FAILED: {}",
                    exception.getMessage()
            );
        }

        filterChain.doFilter(request, response);
    }
}