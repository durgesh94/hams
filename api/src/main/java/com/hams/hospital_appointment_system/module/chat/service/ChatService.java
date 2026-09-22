package com.hams.hospital_appointment_system.module.chat.service;

import org.springframework.stereotype.Service;

import com.hams.hospital_appointment_system.module.chat.dto.ChatRequest;
import com.hams.hospital_appointment_system.module.chat.dto.ChatResponse;

@Service
public interface ChatService {
    ChatResponse chat(ChatRequest request);
}
