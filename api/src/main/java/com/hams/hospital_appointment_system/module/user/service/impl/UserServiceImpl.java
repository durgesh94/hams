package com.hams.hospital_appointment_system.module.user.service.impl;

import com.hams.hospital_appointment_system.module.user.service.UserService;

import lombok.RequiredArgsConstructor;

import com.hams.hospital_appointment_system.common.exception.ResourceNotFoundException;
import com.hams.hospital_appointment_system.module.user.dto.UserResponse;
import com.hams.hospital_appointment_system.module.user.entity.User;
import com.hams.hospital_appointment_system.module.user.mapper.UserMapper;

import java.util.List;
import com.hams.hospital_appointment_system.module.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        return UserMapper.toDto(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        // Implement the logic to get a user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + username));
        return UserMapper.toDto(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        // Implement the logic to get all users
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::toDto).toList();
    }
}
