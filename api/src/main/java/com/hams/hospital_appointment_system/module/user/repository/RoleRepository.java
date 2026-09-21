package com.hams.hospital_appointment_system.module.user.repository;

import com.hams.hospital_appointment_system.module.user.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByName(String name);
}
