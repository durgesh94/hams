package com.hams.auth.config;

import com.hams.auth.entity.Role;
import com.hams.auth.entity.User;
import com.hams.auth.repository.RoleRepository;
import com.hams.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        Role adminRole = createRoleIfNotExists("ADMIN");
        Role operatorRole = createRoleIfNotExists("OPERATOR");

        createUserIfNotExists(
                "admin",
                "Admin@123",
                adminRole
        );

        createUserIfNotExists(
                "operator",
                "Operator@123",
                operatorRole
        );
    }

    private Role createRoleIfNotExists(String roleName) {

        return roleRepository.findByName(roleName)
                .orElseGet(() -> {

                    Role role = Role.builder()
                            .name(roleName)
                            .build();

                    return roleRepository.save(role);
                });
    }

    private void createUserIfNotExists(
            String username,
            String password,
            Role role
    ) {

        if (userRepository.existsByUsername(username)) {
            return;
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build();

        userRepository.save(user);
    }
}