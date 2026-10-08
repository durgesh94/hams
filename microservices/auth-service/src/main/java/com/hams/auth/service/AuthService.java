package com.hams.auth.service;

import com.hams.auth.dto.LoginRequest;
import com.hams.auth.dto.LoginResponse;
import com.hams.auth.dto.RegisterRequest;
import com.hams.auth.dto.UserResponse;
import com.hams.auth.entity.User;
import com.hams.auth.entity.Role;
import com.hams.auth.exception.DuplicateResourceException;
import com.hams.auth.exception.RequiredRoleNotFoundException;
import com.hams.auth.repository.RoleRepository;
import com.hams.auth.repository.UserRepository;
import com.hams.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserResponse createUser(RegisterRequest request, String role) {

        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username already exists");
        }

        Role patientRole = roleRepository
                .findByName(role)
                .orElseThrow(() ->
                        new RequiredRoleNotFoundException(role)
                );

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(patientRole)
                .build();

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole().getName()
        );
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password()
                        )
                );

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        String token = jwtService.generateToken(
                (org.springframework.security.core.userdetails.User)
                        authentication.getPrincipal()
        );

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