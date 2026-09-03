package com.hams.hospital_appointment_system.module.user.mapper;

import com.hams.hospital_appointment_system.module.user.dto.UserRequest;
import com.hams.hospital_appointment_system.module.user.dto.UserResponse;
import com.hams.hospital_appointment_system.module.user.entity.User;

public class UserMapper {

    private UserMapper() {
        // Private constructor to prevent instantiation
    }
    
    public static User toEntity(UserRequest userRequest) {
        return User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .build();
    }

    public static UserResponse toDto(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().getName())
                .build();
    }
}
