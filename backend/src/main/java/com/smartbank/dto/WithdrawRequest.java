package com.smartbank.dto;

public class WithdrawRequest {
    private Double amount;
    private String description;

    public WithdrawRequest() {}
    public WithdrawRequest(Double amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
