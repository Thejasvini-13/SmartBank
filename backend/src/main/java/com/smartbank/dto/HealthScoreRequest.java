package com.smartbank.dto;

public class HealthScoreRequest {
    private Double monthlyIncome;
    private Double monthlyExpenses;
    private Double totalBalance;
    private Double existingEmi;
    private Double goalProgressPercent;
    private Double spendingLimitUsagePercent;

    public HealthScoreRequest() {}

    public Double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(Double monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public Double getMonthlyExpenses() { return monthlyExpenses; }
    public void setMonthlyExpenses(Double monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }

    public Double getTotalBalance() { return totalBalance; }
    public void setTotalBalance(Double totalBalance) { this.totalBalance = totalBalance; }

    public Double getExistingEmi() { return existingEmi; }
    public void setExistingEmi(Double existingEmi) { this.existingEmi = existingEmi; }

    public Double getGoalProgressPercent() { return goalProgressPercent; }
    public void setGoalProgressPercent(Double goalProgressPercent) { this.goalProgressPercent = goalProgressPercent; }

    public Double getSpendingLimitUsagePercent() { return spendingLimitUsagePercent; }
    public void setSpendingLimitUsagePercent(Double spendingLimitUsagePercent) { this.spendingLimitUsagePercent = spendingLimitUsagePercent; }
}
