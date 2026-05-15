package com.growthos.aibackend.controller;

import com.growthos.aibackend.common.Result;
import com.growthos.aibackend.entity.Conversation;
import com.growthos.aibackend.service.ConversationService;
import com.growthos.aibackend.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversation")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @PostMapping("/create")
    public Result create(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        Integer userId = JwtUtils.getUserId(token);
        if (userId == null) {
            // 🔥 修复：只传一个字符串，不报错！
            return Result.error("用户不存在");
        }

        Conversation conversation = conversationService.create(userId);
        return Result.success(conversation);
    }

    @GetMapping("/list")
    public Result list(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Integer userId = JwtUtils.getUserId(token);

        if (userId == null) {
            // 🔥 修复
            return Result.error("用户不存在");
        }

        List<Conversation> list = conversationService.list(userId);
        return Result.success(list);
    }
}