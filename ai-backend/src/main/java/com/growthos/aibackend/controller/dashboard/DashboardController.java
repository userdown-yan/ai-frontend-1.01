package com.growthos.aibackend.controller.dashboard;

import com.growthos.aibackend.common.Result;
import com.growthos.aibackend.dto.dashboard.DashboardSummaryDTO;
import com.growthos.aibackend.service.dashboard.DashboardService;
import com.growthos.aibackend.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public Result<?> summary(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Integer userId = JwtUtils.getUserId(token);
        if (userId == null) {
            return Result.error("用户不存在");
        }
        DashboardSummaryDTO summary = dashboardService.getSummary(userId);
        return Result.success(summary);
    }
}