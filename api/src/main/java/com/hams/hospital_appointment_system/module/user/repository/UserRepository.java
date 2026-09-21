package com.hams.hospital_appointment_system.module.user.repository;

import com.hams.hospital_appointment_system.module.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);

  boolean existsById(Long id);
}
