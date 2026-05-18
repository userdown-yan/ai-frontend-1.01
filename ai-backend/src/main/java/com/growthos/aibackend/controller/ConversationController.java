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
            return Result.error("用户不存在");
        }

        List<Conversation> list = conversationService.list(userId);
        return Result.success(list);
    }

    @PostMapping("/rename")
    public Result rename(
            HttpServletRequest request,
            @RequestParam Integer id,
            @RequestParam String title
    ) {
        String token = request.getHeader("Authorization");
        Integer userId = JwtUtils.getUserId(token);
        if (userId == null) {
            return Result.error("用户不存在");
        }

        String trimmedTitle = title == null ? "" : title.trim();
        if (trimmedTitle.isEmpty()) {
            return Result.error("标题不能为空");
        }

        boolean updated = conversationService.updateTitle(id, userId, trimmedTitle);
        return updated ? Result.success("重命名成功") : Result.error("会话不存在");
    }

    @DeleteMapping("/{id}")
    public Result delete(HttpServletRequest request, @PathVariable Integer id) {
        String token = request.getHeader("Authorization");
        Integer userId = JwtUtils.getUserId(token);
        if (userId == null) {
            return Result.error("用户不存在");
        }

        boolean deleted = conversationService.delete(id, userId);
        return deleted ? Result.success("删除成功") : Result.error("会话不存在");
    }
}