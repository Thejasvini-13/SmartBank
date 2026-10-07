package com.smartbank.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "account_number", nullable = false, length = 20)
    private String accountNumber;

    @Column(nullable = false, length = 10)
    private String type; // CREDIT, DEBIT

    @Column(nullable = false)
    private Double amount;

    @Column(length = 50)
    private String category; // FOOD, TRAVEL, SHOPPING, BILLS, ENTERTAINMENT, HEALTH, EDUCATION, OTHER

    @Column(length = 255)
    private String description;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "is_suspicious")
    private Boolean isSuspicious = false;

    @Column(name = "risk_reason", length = 255)
    private String riskReason;

    @PrePersist
    protected void onCreate() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }

    public Transaction() {}

    public Transaction(String accountNumber, String type, Double amount, String category, String description, Boolean isSuspicious, String riskReason) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.isSuspicious = isSuspicious;
        this.riskReason = riskReason;
    }

    public Integer getTransactionId() { return transactionId; }
    public void setTransactionId(Integer transactionId) { this.transactionId = transactionId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Boolean getIsSuspicious() { return isSuspicious; }
    public void setIsSuspicious(Boolean isSuspicious) { this.isSuspicious = isSuspicious; }

    public String getRiskReason() { return riskReason; }
    public void setRiskReason(String riskReason) { this.riskReason = riskReason; }
}
