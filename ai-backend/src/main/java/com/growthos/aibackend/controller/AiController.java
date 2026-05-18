package com.growthos.aibackend.controller;

import okhttp3.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.TimeUnit;

@RestController
public class AiController {

    private static final String VOLC_API_KEY = "0b7b821e-a6d1-433b-91e8-4cadf932f0c1";
    private static final String VOLC_API_URL = "https://ark.cn-beijing.volces.com/api/v3/chat/completions";
    private static final String MODEL = "doubao-1.5-pro";

    @PostMapping("/ai/chat/stream")
    public SseEmitter chatStream(@RequestParam String message) {
        SseEmitter emitter = new SseEmitter(0L); // 不超时

        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .build();

                String jsonBody = "{"
                        + "\"model\":\"" + MODEL + "\","
                        + "\"stream\":true,"
                        + "\"messages\":[{"
                        + "\"role\":\"user\","
                        + "\"content\":\"" + escapeJson(message) + "\""
                        + "}]"
                        + "}";

                Request request = new Request.Builder()
                        .url(VOLC_API_URL)
                        .header("Authorization", "Bearer " + VOLC_API_KEY)
                        .post(RequestBody.create(
                                MediaType.parse("application/json"),
                                jsonBody
                        ))
                        .build();

                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                if (responseBody == null) {
                    emitter.send("请求失败");
                    emitter.complete();
                    return;
                }

                String line;
                while ((line = responseBody.source().readUtf8Line()) != null) {
                    if (line.isEmpty() || !line.startsWith("data:")) continue;
                    String data = line.substring(5).trim();
                    if ("[DONE]".equals(data)) break;

                    String content = extractContent(data);
                    if (content != null && !content.isEmpty()) {
                        emitter.send(content); // 极速输出，不等待
                    }
                }

                emitter.complete();

            } catch (Exception e) {
                try {
                    emitter.send("出错：" + e.getMessage());
                } catch (Exception ignored) {}
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }

    private String extractContent(String data) {
        try {
            if (data.contains("\"delta\":")) {
                int idx = data.indexOf("\"content\":\"");
                if (idx == -1) return null;
                int start = idx + 11;
                int end = data.indexOf("\"", start);
                return end > start ? data.substring(start, end).replace("\\n", "\n") : null;
            }
        } catch (Exception ignored) {}
        return null;
    }

    private String escapeJson(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}