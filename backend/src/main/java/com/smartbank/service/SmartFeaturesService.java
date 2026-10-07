package com.smartbank.service;

import com.smartbank.entity.LoanApplication;
import com.smartbank.entity.SavingsGoal;
import com.smartbank.entity.ScheduledPayment;
import com.smartbank.entity.SpendingLimit;
import com.smartbank.dto.LoanRequest;
import com.smartbank.dto.HealthScoreRequest;
import com.smartbank.exception.ResourceNotFoundException;
import com.smartbank.repository.LoanApplicationRepository;
import com.smartbank.repository.SavingsGoalRepository;
import com.smartbank.repository.ScheduledPaymentRepository;
import com.smartbank.repository.SpendingLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class SmartFeaturesService {

    @Autowired
    private SavingsGoalRepository savingsGoalRepository;

    @Autowired
    private SpendingLimitRepository spendingLimitRepository;

    @Autowired
    private ScheduledPaymentRepository scheduledPaymentRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    // --- 1. SAVINGS GOAL TRACKER ---
    public SavingsGoal createSavingsGoal(SavingsGoal goal) {
        return savingsGoalRepository.save(goal);
    }

    public List<SavingsGoal> getSavingsGoalsByCustomer(Integer customerId) {
        return savingsGoalRepository.findByCustomerId(customerId);
    }

    public List<SavingsGoal> getAllSavingsGoals() {
        return savingsGoalRepository.findAll();
    }

    public SavingsGoal updateSavingsGoal(Integer id, Double addedAmount) {
        SavingsGoal goal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Savings goal not found: " + id));
        goal.setCurrentAmount(goal.getCurrentAmount() + addedAmount);
        return savingsGoalRepository.save(goal);
    }

    public void deleteSavingsGoal(Integer id) {
        savingsGoalRepository.deleteById(id);
    }

    public Map<String, Object> calculateGoalDetails(SavingsGoal goal) {
        double remaining = Math.max(0, goal.getTargetAmount() - goal.getCurrentAmount());
        double percentage = Math.min(100.0, (goal.getCurrentAmount() / goal.getTargetAmount()) * 100.0);

        long monthsLeft = ChronoUnit.MONTHS.between(LocalDate.now(), goal.getDeadline());
        if (monthsLeft <= 0) monthsLeft = 1;
        double recommendedMonthly = remaining / monthsLeft;

        Map<String, Object> map = new HashMap<>();
        map.put("goal", goal);
        map.put("remainingAmount", remaining);
        map.put("progressPercentage", Math.round(percentage * 10.0) / 10.0);
        map.put("recommendedMonthlySaving", Math.round(recommendedMonthly * 100.0) / 100.0);
        return map;
    }

    // --- 2. SPENDING LIMIT ALERTS ---
    public SpendingLimit setOrUpdateSpendingLimit(SpendingLimit limit) {
        Optional<SpendingLimit> existing = spendingLimitRepository.findByAccountNumber(limit.getAccountNumber());
        if (existing.isPresent()) {
            SpendingLimit s = existing.get();
            s.setMonthlyLimit(limit.getMonthlyLimit());
            return spendingLimitRepository.save(s);
        }
        return spendingLimitRepository.save(limit);
    }

    public SpendingLimit getSpendingLimit(String accountNumber) {
        return spendingLimitRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Spending limit not set for account: " + accountNumber));
    }

    public Map<String, Object> evaluateSpendingLimitStatus(String accountNumber) {
        SpendingLimit limit = getSpendingLimit(accountNumber);
        double usage = (limit.getCurrentSpending() / limit.getMonthlyLimit()) * 100.0;
        String status;
        String alertMessage;

        if (usage > 100) {
            status = "LIMIT EXCEEDED";
            alertMessage = "CRITICAL WARNING: You have exceeded your monthly spending limit!";
        } else if (usage >= 90) {
            status = "CRITICAL";
            alertMessage = "Warning: You have reached " + String.format("%.1f", usage) + "% of your monthly limit.";
        } else if (usage >= 70) {
            status = "WARNING";
            alertMessage = "Notice: You have used " + String.format("%.1f", usage) + "% of your monthly limit.";
        } else {
            status = "NORMAL";
            alertMessage = "Spending is healthy and within budget limits.";
        }

        Map<String, Object> res = new HashMap<>();
        res.put("accountNumber", accountNumber);
        res.put("monthlyLimit", limit.getMonthlyLimit());
        res.put("currentSpending", limit.getCurrentSpending());
        res.put("usagePercentage", Math.round(usage * 10.0) / 10.0);
        res.put("alertLevel", status);
        res.put("message", alertMessage);
        return res;
    }

    // --- 3. SCHEDULED PAYMENTS ---
    public ScheduledPayment schedulePayment(ScheduledPayment payment) {
        return scheduledPaymentRepository.save(payment);
    }

    public List<ScheduledPayment> getScheduledPayments(String accountNumber) {
        return scheduledPaymentRepository.findByAccountNumber(accountNumber);
    }

    public List<ScheduledPayment> getAllScheduledPayments() {
        return scheduledPaymentRepository.findAll();
    }

    public void cancelScheduledPayment(Integer id) {
        scheduledPaymentRepository.deleteById(id);
    }

    // --- 4. LOAN ELIGIBILITY CALCULATOR ---
    public LoanApplication evaluateLoanEligibility(LoanRequest req) {
        double income = req.getMonthlyIncome() != null ? req.getMonthlyIncome() : 1.0;
        double expenses = req.getMonthlyExpenses() != null ? req.getMonthlyExpenses() : 0.0;
        double existingEmi = req.getExistingEmi() != null ? req.getExistingEmi() : 0.0;
        int creditScore = req.getCreditScore() != null ? req.getCreditScore() : 600;
        double requestedAmount = req.getRequestedAmount() != null ? req.getRequestedAmount() : 100000.0;
        int tenure = req.getTenureMonths() != null ? req.getTenureMonths() : 24;

        double monthlyRate = 0.105 / 12;
        double estimatedEmi = (requestedAmount * monthlyRate * Math.pow(1 + monthlyRate, tenure)) /
                              (Math.pow(1 + monthlyRate, tenure) - 1);

        double totalObligations = existingEmi + estimatedEmi;
        double dti = (totalObligations / income) * 100.0;

        boolean eligible = true;
        StringBuilder explanation = new StringBuilder();

        if (creditScore < 650) {
            eligible = false;
            explanation.append("Credit score (").append(creditScore).append(") is below required threshold of 650. ");
        } else {
            explanation.append("Credit score (").append(creditScore).append(") satisfies minimum requirement. ");
        }

        if (dti > 50.0) {
            eligible = false;
            explanation.append(String.format("Debt-to-income ratio (%.1f%%) exceeds maximum allowed 50%% cap. ", dti));
        } else {
            explanation.append(String.format("Debt-to-income ratio (%.1f%%) is healthy. ", dti));
        }

        if (income - expenses - totalObligations < 5000) {
            eligible = false;
            explanation.append("Net disposable income after proposed EMI is less than ₹5,000 safety margin. ");
        }

        if (eligible) {
            explanation.append("Income and credit profile satisfy educational eligibility rules.");
        }

        LoanApplication app = new LoanApplication();
        app.setCustomerId(req.getCustomerId() != null ? req.getCustomerId() : 1);
        app.setMonthlyIncome(income);
        app.setMonthlyExpenses(expenses);
        app.setExistingEmi(existingEmi);
        app.setCreditScore(creditScore);
        app.setRequestedAmount(requestedAmount);
        app.setTenureMonths(tenure);
        app.setStatus(eligible ? "ELIGIBLE" : "NOT_ELIGIBLE");
        app.setEstimatedEmi(Math.round(estimatedEmi * 100.0) / 100.0);
        app.setDebtToIncomeRatio(Math.round(dti * 10.0) / 10.0);
        app.setExplanation(explanation.toString().trim());

        return loanApplicationRepository.save(app);
    }

    // --- 5. FINANCIAL HEALTH SCORE ENGINE ---
    public Map<String, Object> calculateFinancialHealth(HealthScoreRequest req) {
        double income = req.getMonthlyIncome() != null ? req.getMonthlyIncome() : 50000.0;
        double expenses = req.getMonthlyExpenses() != null ? req.getMonthlyExpenses() : 25000.0;
        double balance = req.getTotalBalance() != null ? req.getTotalBalance() : 100000.0;
        double emi = req.getExistingEmi() != null ? req.getExistingEmi() : 0.0;
        double goalProgress = req.getGoalProgressPercent() != null ? req.getGoalProgressPercent() : 50.0;
        double limitUsage = req.getSpendingLimitUsagePercent() != null ? req.getSpendingLimitUsagePercent() : 50.0;

        double savingsRate = ((income - expenses) / income) * 100.0;
        int savingsScore = (int) Math.min(25, (savingsRate / 30.0) * 25);

        int spendingScore;
        if (limitUsage <= 70) spendingScore = 25;
        else if (limitUsage <= 90) spendingScore = 15;
        else if (limitUsage <= 100) spendingScore = 8;
        else spendingScore = 0;

        double dti = (emi / income) * 100.0;
        int debtScore;
        if (dti == 0) debtScore = 25;
        else if (dti <= 20) debtScore = 20;
        else if (dti <= 40) debtScore = 12;
        else debtScore = 0;

        int goalScore = (int) Math.min(25, (goalProgress / 100.0) * 25);

        int total = savingsScore + spendingScore + debtScore + goalScore;

        String category;
        if (total >= 80) category = "EXCELLENT";
        else if (total >= 60) category = "GOOD";
        else if (total >= 40) category = "NEEDS IMPROVEMENT";
        else category = "CRITICAL";

        List<String> recommendations = new ArrayList<>();
        if (savingsScore < 15) recommendations.add("Aim to save at least 20-30% of your monthly income into emergency funds.");
        if (spendingScore < 15) recommendations.add("Review monthly spending limits to avoid entering warning thresholds.");
        if (debtScore < 15) recommendations.add("High EMI burden detected. Consider paying off high-interest loans first.");
        if (goalScore < 15) recommendations.add("Allocate dedicated monthly savings towards your financial goals.");
        if (recommendations.isEmpty()) recommendations.add("Great financial discipline! Keep building your long-term savings.");

        Map<String, Object> result = new HashMap<>();
        result.put("overallScore", total);
        result.put("category", category);
        result.put("savingsScore", savingsScore * 4);
        result.put("spendingScore", spendingScore * 4);
        result.put("debtScore", debtScore * 4);
        result.put("goalScore", goalScore * 4);
        result.put("recommendations", recommendations);
        return result;
    }
}
