package com.hams.auth.service;

import com.hams.auth.dto.LoginRequest;
import com.hams.auth.dto.LoginResponse;
import com.hams.auth.dto.UserResponse;
import com.hams.auth.entity.User;
import com.hams.auth.repository.UserRepository;
import com.hams.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        System.out.println("AUTH LOGIN: " + request.username());

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password()
                        )
                );

        System.out.println(
                "AUTHENTICATION SUCCESS: "
                        + authentication.getName()
        );

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        String token = jwtService.generateToken(
                (org.springframework.security.core.userdetails.User)
                        authentication.getPrincipal()
        );

        System.out.println("JWT GENERATED");

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole().getName()
        );

        return new LoginResponse(
                token,
                "Bearer",
                jwtService.getExpirationInSeconds(),
                userResponse
        );
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String username) {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow();

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole().getName()
        );
    }
}