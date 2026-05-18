package com.growthos.aibackend.controller;

import com.growthos.aibackend.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductPlanController {

    @GetMapping("/models")
    public Result<List<String>> models() {
        return Result.success(List.of("gpt-4o-mini", "gpt-4.1", "deepseek-chat"));
    }

    @GetMapping("/templates")
    public Result<List<Map<String, String>>> templates() {
        return Result.success(List.of(
                Map.of("name", "日报总结", "prompt", "请将对话整理为日报结构"),
                Map.of("name", "任务拆解", "prompt", "请分步骤拆解任务并给出预计耗时"),
                Map.of("name", "代码审查", "prompt", "请给出风险、建议、优先级")
        ));
    }

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        return Result.success(Map.of(
                "chatCount", 168,
                "activeDays", 24,
                "goalProgress", 0.68,
                "weeklyTrend", List.of(5, 8, 9, 12, 10, 15, 18)
        ));
    }

    @GetMapping("/tasks")
    public Result<List<Map<String, Object>>> tasks() {
        return Result.success(List.of(
                Map.of("id", 1, "title", "UI 主题升级", "status", "IN_PROGRESS", "progress", 0.75),
                Map.of("id", 2, "title", "对话与任务关联", "status", "TODO", "progress", 0.2)
        ));
    }

    @GetMapping("/growth")
    public Result<Map<String, Object>> growthLoop() {
        return Result.success(Map.of(
                "habitStreak", 12,
                "readingHours", 36,
                "reviewGenerated", true
        ));
    }
}