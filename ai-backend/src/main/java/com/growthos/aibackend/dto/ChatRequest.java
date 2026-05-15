package com.growthos.aibackend.dto;

import lombok.Data;

@Data
public class ChatRequest {

    private String message;

    // 会话ID
    private Long conversationId;
}