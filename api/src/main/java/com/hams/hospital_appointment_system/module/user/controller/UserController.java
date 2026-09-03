package com.hams.hospital_appointment_system.module.user.controller;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hams.hospital_appointment_system.common.response.ApiResponse;
import com.hams.hospital_appointment_system.module.user.dto.UserResponse;
import com.hams.hospital_appointment_system.module.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(){
        List<UserResponse> users =  userService.getAllUsers();
        ApiResponse<List<UserResponse>> response = ApiResponse.<List<UserResponse>>builder()
        .status(HttpStatus.OK.value())
        .message("Fetched all users successfully")
        .data(users)
        .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/id")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@RequestParam Long id){
        UserResponse user = userService.getUserById(id);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
        .status(HttpStatus.OK.value())
        .message("Fetched user successfully")
        .data(user)
        .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/username")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUsername(@RequestParam String username){
        UserResponse user = userService.getUserByUsername(username);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
        .status(HttpStatus.OK.value())
        .message("Fetched user successfully")
        .data(user)
        .build();

        return ResponseEntity.ok(response);
    }
}
