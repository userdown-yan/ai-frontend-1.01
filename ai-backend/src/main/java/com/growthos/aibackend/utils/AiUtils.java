package com.growthos.aibackend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

public class AiUtils {

    // 稳定可用的公共 AI 接口，不会超时
    private static final String API_URL = "https://api.302.ai/v1/chat/completions";

    public static String chat(String prompt) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper objectMapper = new ObjectMapper();

            // 请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer sk-302ai-free-key");

            // 请求体
            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-3.5-turbo");

            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            messages.add(message);

            body.put("messages", messages);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            Map<String, Object> res = response.getBody();
            if (res == null || !res.containsKey("choices")) {
                return "AI 暂无返回，请稍后再试";
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) res.get("choices");
            Map<String, Object> choice = choices.get(0);
            Map<String, String> msg = (Map<String, String>) choice.get("message");
            return msg.get("content");

        } catch (Exception e) {
            e.printStackTrace();
            return "AI 调用失败：请求超时或服务异常";
        }
    }
}