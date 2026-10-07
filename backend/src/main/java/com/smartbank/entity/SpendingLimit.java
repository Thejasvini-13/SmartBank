package com.smartbank.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "spending_limits")
public class SpendingLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "limit_id")
    private Integer limitId;

    @Column(name = "account_number", nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Column(name = "monthly_limit", nullable = false)
    private Double monthlyLimit;

    @Column(name = "current_spending")
    private Double currentSpending = 0.0;

    public SpendingLimit() {}

    public SpendingLimit(Integer limitId, String accountNumber, Double monthlyLimit, Double currentSpending) {
        this.limitId = limitId;
        this.accountNumber = accountNumber;
        this.monthlyLimit = monthlyLimit;
        this.currentSpending = currentSpending;
    }

    public Integer getLimitId() { return limitId; }
    public void setLimitId(Integer limitId) { this.limitId = limitId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public Double getMonthlyLimit() { return monthlyLimit; }
    public void setMonthlyLimit(Double monthlyLimit) { this.monthlyLimit = monthlyLimit; }

    public Double getCurrentSpending() { return currentSpending; }
    public void setCurrentSpending(Double currentSpending) { this.currentSpending = currentSpending; }
}
