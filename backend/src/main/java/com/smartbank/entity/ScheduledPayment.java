package com.smartbank.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "scheduled_payments")
public class ScheduledPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @Column(name = "account_number", nullable = false, length = 20)
    private String accountNumber;

    @Column(name = "payee_name", nullable = false, length = 100)
    private String payeeName;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false, length = 20)
    private String frequency; // MONTHLY, WEEKLY, YEARLY

    @Column(name = "next_payment_date", nullable = false)
    private LocalDate nextPaymentDate;

    @Column(length = 20)
    private String status = "ACTIVE";

    public ScheduledPayment() {}

    public ScheduledPayment(Integer paymentId, String accountNumber, String payeeName, Double amount, String frequency, LocalDate nextPaymentDate, String status) {
        this.paymentId = paymentId;
        this.accountNumber = accountNumber;
        this.payeeName = payeeName;
        this.amount = amount;
        this.frequency = frequency;
        this.nextPaymentDate = nextPaymentDate;
        this.status = status;
    }

    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getPayeeName() { return payeeName; }
    public void setPayeeName(String payeeName) { this.payeeName = payeeName; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public LocalDate getNextPaymentDate() { return nextPaymentDate; }
    public void setNextPaymentDate(LocalDate nextPaymentDate) { this.nextPaymentDate = nextPaymentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
