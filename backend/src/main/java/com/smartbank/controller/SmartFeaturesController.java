package com.smartbank.controller;

import com.smartbank.entity.LoanApplication;
import com.smartbank.entity.SavingsGoal;
import com.smartbank.entity.ScheduledPayment;
import com.smartbank.entity.SpendingLimit;
import com.smartbank.dto.LoanRequest;
import com.smartbank.dto.HealthScoreRequest;
import com.smartbank.service.SmartFeaturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/smart")
@CrossOrigin(origins = "*")
public class SmartFeaturesController {

    @Autowired
    private SmartFeaturesService smartFeaturesService;

    // --- 1. SAVINGS GOALS ---
    @PostMapping("/goals")
    public ResponseEntity<SavingsGoal> createSavingsGoal(@RequestBody SavingsGoal goal) {
        SavingsGoal created = smartFeaturesService.createSavingsGoal(goal);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/goals")
    public ResponseEntity<List<SavingsGoal>> getAllGoals() {
        return ResponseEntity.ok(smartFeaturesService.getAllSavingsGoals());
    }

    @GetMapping("/goals/customer/{customerId}")
    public ResponseEntity<List<SavingsGoal>> getGoalsByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(smartFeaturesService.getSavingsGoalsByCustomer(customerId));
    }

    @PutMapping("/goals/{id}/deposit")
    public ResponseEntity<SavingsGoal> updateGoalProgress(@PathVariable Integer id, @RequestParam Double amount) {
        return ResponseEntity.ok(smartFeaturesService.updateSavingsGoal(id, amount));
    }

    @DeleteMapping("/goals/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Integer id) {
        smartFeaturesService.deleteSavingsGoal(id);
        return ResponseEntity.noContent().build();
    }

    // --- 2. SPENDING LIMITS ---
    @PostMapping("/limits")
    public ResponseEntity<SpendingLimit> setSpendingLimit(@RequestBody SpendingLimit limit) {
        return ResponseEntity.ok(smartFeaturesService.setOrUpdateSpendingLimit(limit));
    }

    @GetMapping("/limits/{accountNumber}")
    public ResponseEntity<Map<String, Object>> getSpendingLimitStatus(@PathVariable String accountNumber) {
        return ResponseEntity.ok(smartFeaturesService.evaluateSpendingLimitStatus(accountNumber));
    }

    // --- 3. SCHEDULED PAYMENTS ---
    @PostMapping("/scheduled")
    public ResponseEntity<ScheduledPayment> schedulePayment(@RequestBody ScheduledPayment payment) {
        ScheduledPayment created = smartFeaturesService.schedulePayment(payment);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/scheduled")
    public ResponseEntity<List<ScheduledPayment>> getAllScheduledPayments() {
        return ResponseEntity.ok(smartFeaturesService.getAllScheduledPayments());
    }

    @GetMapping("/scheduled/{accountNumber}")
    public ResponseEntity<List<ScheduledPayment>> getScheduledPaymentsByAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(smartFeaturesService.getScheduledPayments(accountNumber));
    }

    @DeleteMapping("/scheduled/{id}")
    public ResponseEntity<Void> cancelScheduledPayment(@PathVariable Integer id) {
        smartFeaturesService.cancelScheduledPayment(id);
        return ResponseEntity.noContent().build();
    }

    // --- 4. LOAN ELIGIBILITY ---
    @PostMapping("/loan/calculate")
    public ResponseEntity<LoanApplication> evaluateLoan(@RequestBody LoanRequest req) {
        LoanApplication app = smartFeaturesService.evaluateLoanEligibility(req);
        return ResponseEntity.ok(app);
    }

    // --- 5. FINANCIAL HEALTH SCORE ---
    @PostMapping("/health/calculate")
    public ResponseEntity<Map<String, Object>> calculateHealth(@RequestBody HealthScoreRequest req) {
        Map<String, Object> report = smartFeaturesService.calculateFinancialHealth(req);
        return ResponseEntity.ok(report);
    }
}
