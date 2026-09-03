package com.hams.hospital_appointment_system.module.user.service;

import com.hams.hospital_appointment_system.module.user.dto.UserResponse;
import java.util.List;

public interface UserService {

    public UserResponse getUserById(Long id);

    public UserResponse getUserByUsername(String username);

    public List<UserResponse> getAllUsers();
    
}
