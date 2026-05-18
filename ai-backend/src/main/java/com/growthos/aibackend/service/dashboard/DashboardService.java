package com.growthos.aibackend.service.dashboard;

import com.growthos.aibackend.dto.dashboard.DashboardSummaryDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardService {

    public DashboardSummaryDTO getSummary(Integer userId) {
        DashboardSummaryDTO dto = new DashboardSummaryDTO();
        dto.setTodayDate(LocalDate.now());
        dto.setAiAdvice("今天优先完成 1 个高价值任务，并在晚间 22:30 前停止信息摄入。");
        dto.setTaskCompletionRate(68);
        dto.setFocusHours(3.5);
        dto.setHabitCompleted(3);
        dto.setHabitTotal(5);
        dto.setGoalProgress(42);
        dto.setWeeklyTrends(List.of(
                "本周学习时长较上周提升 18%。",
                "下午 14:00-17:00 是你的高效率时段。",
                "连续熬夜两天后，次日任务完成率平均下降 22%。"
        ));
        dto.setRisks(List.of(
                "最近三天晚睡，专注力出现下滑。",
                "英语学习连续两天缺失，长期目标可能偏航。"
        ));
        dto.setTodayActions(List.of(
                "先完成最重要的 1 个任务（MIT）。",
                "安排 2 组 45 分钟深度专注时间块。",
                "晚间进行 5 分钟日复盘，记录情绪与进展。"
        ));
        return dto;
    }
}