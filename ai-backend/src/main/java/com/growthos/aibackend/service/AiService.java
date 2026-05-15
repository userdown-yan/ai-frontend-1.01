package com.growthos.aibackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growthos.aibackend.dto.ChatRequest;
import com.growthos.aibackend.entity.Message;
import com.growthos.aibackend.mapper.MessageMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AiService {

    // 火山引擎配置
    private static final String API_KEY = "0b7b821e-a6d1-433b-91e8-4cadf932f0c1";
    private static final String ENDPOINT_ID = "ep-20260507214223-ql662";

    @Autowired
    private MessageMapper messageMapper;

    // ====================== 修复超时 ======================
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String chat(ChatRequest request) {
        try {
            // 1. 获取历史消息
            List<Message> history = messageMapper.findByConversationId(request.getConversationId());

            // 2. 构造上下文
            List<Map<String, String>> messages = new ArrayList<>();
            for (Message msg : history) {
                Map<String, String> map = new HashMap<>();
                map.put("role", msg.getRole());
                map.put("content", msg.getContent());
                messages.add(map);
            }

            // 当前用户消息
            Map<String, String> userMsgMap = new HashMap<>();
            userMsgMap.put("role", "user");
            userMsgMap.put("content", request.getMessage());
            messages.add(userMsgMap);

            // 3. 保存用户消息
            Message userMsg = new Message();
            userMsg.setConversationId(request.getConversationId());
            userMsg.setRole("user");
            userMsg.setContent(request.getMessage());
            messageMapper.insert(userMsg);

            // 4. 构造AI请求
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("model", ENDPOINT_ID);
            bodyMap.put("messages", messages);

            String json = objectMapper.writeValueAsString(bodyMap);

            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json")
            );

            Request httpRequest = new Request.Builder()
                    .url("https://ark.cn-beijing.volces.com/api/v3/chat/completions")
                    .post(body)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .build();

            // 5. 调用AI
            Response response = client.newCall(httpRequest).execute();
            String resp = response.body().string();
            System.out.println("AI 返回：" + resp);

            JsonNode root = objectMapper.readTree(resp);

            if (root.has("error")) {
                return "AI错误：" + root.get("error").get("message").asText();
            }

            // 6. 解析回复
            String aiReply = root.get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText();

            // 7. 保存AI消息
            Message aiMsg = new Message();
            aiMsg.setConversationId(request.getConversationId());
            aiMsg.setRole("assistant");
            aiMsg.setContent(aiReply);
            messageMapper.insert(aiMsg);

            return aiReply;

        } catch (Exception e) {
            e.printStackTrace();
            return "AI调用失败：" + e.getMessage();
        }
    }
}