package com.hams.hospital_appointment_system.common.config;

import com.hams.hospital_appointment_system.module.user.entity.Role;
import com.hams.hospital_appointment_system.module.user.entity.User;
import com.hams.hospital_appointment_system.module.user.repository.RoleRepository;
import com.hams.hospital_appointment_system.module.user.repository.UserRepository;
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

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new IllegalStateException("Admin role not found"));
        Role operatorRole = roleRepository.findByName("OPERATOR")
                .orElseThrow(() -> new IllegalStateException("Operator role not found"));

        createUserIfNotExists("admin","Admin@123", adminRole);
        createUserIfNotExists("operator", "Operator@123", operatorRole);
    }

    private void createUserIfNotExists(String username, String password, Role role) {
        if(userRepository.existsByUsername(username)){
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        userRepository.save(user);
    }
}
