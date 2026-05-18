package com.growthos.aibackend.dto.dashboard;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DashboardSummaryDTO {
    private LocalDate todayDate;
    private String aiAdvice;
    private Integer taskCompletionRate;
    private Double focusHours;
    private Integer habitCompleted;
    private Integer habitTotal;
    private Integer goalProgress;
    private List<String> weeklyTrends;
    private List<String> risks;
    private List<String> todayActions;
}