package com.growthos.aibackend.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/ai/chat")
@CrossOrigin
public class ChatController {

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestParam String message) {

        SseEmitter emitter = new SseEmitter(0L); // 不超时

        new Thread(() -> {
            try {
                // 模拟AI回复
                String aiReply = "你好，我是Growth OS AI助手，很高兴帮你解决问题：" + message;

                for (char c : aiReply.toCharArray()) {
                    emitter.send(String.valueOf(c));
                    Thread.sleep(30); // 打字效果
                }

                emitter.complete();

            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }
}