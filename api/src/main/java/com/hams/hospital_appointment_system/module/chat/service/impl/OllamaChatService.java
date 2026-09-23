package com.hams.hospital_appointment_system.module.chat.service.impl;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import com.hams.hospital_appointment_system.module.chat.dto.ChatRequest;
import com.hams.hospital_appointment_system.module.chat.dto.ChatResponse;
import com.hams.hospital_appointment_system.module.chat.service.ChatService;

@Slf4j
@Service
public class OllamaChatService implements ChatService {

    @Override
    public ChatResponse chat(ChatRequest request) {
        log.info("Chat request received: {}", request.getMessage());

        ChatResponse response = ChatResponse.builder()
                .message("This is a response from OllamaChatService: " + request.getMessage())
                .build();

        log.info("Chat response generated: {}", response.getMessage());

        return response;
    }
}
