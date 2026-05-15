package com.growthos.aibackend.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Conversation {

    private Integer id;

    private Integer userId;

    private String title;

    private LocalDateTime createTime;
}