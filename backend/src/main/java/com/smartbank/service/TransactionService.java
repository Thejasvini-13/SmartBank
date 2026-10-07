package com.smartbank.service;

import com.smartbank.entity.Transaction;
import com.smartbank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction recordTransaction(String accountNumber, String type, Double amount, String defaultCategory, String description) {
        String category = categorize(description != null ? description : defaultCategory);

        // Rule-based Suspicious Detection
        boolean isSuspicious = false;
        String riskReason = null;

        if (amount > 50000.0) {
            isSuspicious = true;
            riskReason = "Transaction amount (₹" + String.format("%.2f", amount) + ") exceeds single transaction safety threshold of ₹50,000.00";
        }

        Transaction tx = new Transaction(accountNumber, type.toUpperCase(), amount, category, description, isSuspicious, riskReason);
        return transactionRepository.save(tx);
    }

    public String categorize(String description) {
        if (description == null || description.trim().isEmpty()) {
            return "OTHER";
        }
        String desc = description.toLowerCase(Locale.ROOT);

        if (desc.contains("swiggy") || desc.contains("zomato") || desc.contains("restaurant") || desc.contains("food") || desc.contains("starbucks") || desc.contains("kfc") || desc.contains("mcdonald")) {
            return "FOOD";
        }
        if (desc.contains("uber") || desc.contains("ola") || desc.contains("flight") || desc.contains("train") || desc.contains("petrol") || desc.contains("fuel") || desc.contains("irctc")) {
            return "TRAVEL";
        }
        if (desc.contains("amazon") || desc.contains("flipkart") || desc.contains("myntra") || desc.contains("shopping") || desc.contains("mall") || desc.contains("zara")) {
            return "SHOPPING";
        }
        if (desc.contains("electricity") || desc.contains("water") || desc.contains("bill") || desc.contains("recharge") || desc.contains("wifi") || desc.contains("broadband")) {
            return "BILLS";
        }
        if (desc.contains("netflix") || desc.contains("spotify") || desc.contains("cinema") || desc.contains("movie") || desc.contains("prime") || desc.contains("hotstar")) {
            return "ENTERTAINMENT";
        }
        if (desc.contains("hospital") || desc.contains("pharmacy") || desc.contains("doctor") || desc.contains("medicine") || desc.contains("apollo")) {
            return "HEALTH";
        }
        if (desc.contains("udemy") || desc.contains("coursera") || desc.contains("school") || desc.contains("college") || desc.contains("tuition") || desc.contains("books")) {
            return "EDUCATION";
        }

        return "OTHER";
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        return transactionRepository.findByAccountNumberOrderByTimestampDesc(accountNumber);
    }

    public List<Transaction> filterTransactions(String accountNumber, String category, String type, Double minAmount, Double maxAmount) {
        return transactionRepository.filterTransactions(accountNumber, category, type, minAmount, maxAmount);
    }

    public List<Transaction> getSuspiciousTransactions() {
        return transactionRepository.findByIsSuspiciousTrueOrderByTimestampDesc();
    }
}
