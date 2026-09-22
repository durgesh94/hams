package com.hams.hospital_appointment_system.module.chat.service.impl;

import org.springframework.stereotype.Service;

import com.hams.hospital_appointment_system.module.chat.dto.ChatRequest;
import com.hams.hospital_appointment_system.module.chat.dto.ChatResponse;
import com.hams.hospital_appointment_system.module.chat.service.ChatService;

@Service
public class OllamaChatService implements ChatService {

    @Override
    public ChatResponse chat(ChatRequest request) {
        ChatResponse response = ChatResponse.builder()
                .message("This is a response from OllamaChatService: " + request.getMessage())
                .build();
        return response;
    }
}
