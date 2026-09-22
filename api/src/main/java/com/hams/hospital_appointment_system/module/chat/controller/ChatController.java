package com.hams.hospital_appointment_system.module.chat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import java.net.http.HttpClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hams.hospital_appointment_system.common.response.ApiResponse;
import com.hams.hospital_appointment_system.module.chat.dto.ChatRequest;
import com.hams.hospital_appointment_system.module.chat.dto.ChatResponse;
import com.hams.hospital_appointment_system.module.chat.service.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatResponse>> chat(@RequestBody ChatRequest request) {
        ChatResponse chatResponse = chatService.chat(request);
        ApiResponse<ChatResponse> apiResponse = ApiResponse.<ChatResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Chat request processed successfully")
                .data(chatResponse)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

}
