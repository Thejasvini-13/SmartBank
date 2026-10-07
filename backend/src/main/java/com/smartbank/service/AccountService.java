package com.smartbank.service;

import com.smartbank.entity.Account;
import com.smartbank.entity.Customer;
import com.smartbank.entity.Transaction;
import com.smartbank.entity.SpendingLimit;
import com.smartbank.exception.InsufficientBalanceException;
import com.smartbank.exception.InvalidOperationException;
import com.smartbank.exception.ResourceNotFoundException;
import com.smartbank.repository.AccountRepository;
import com.smartbank.repository.SpendingLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SpendingLimitRepository spendingLimitRepository;

    public Account createAccount(Integer customerId, String accountType, Double initialDeposit) {
        Customer customer = customerService.getCustomerById(customerId);

        if (initialDeposit == null || initialDeposit < 0) {
            throw new InvalidOperationException("Initial deposit amount cannot be negative.");
        }

        String accNo = ("SAVINGS".equalsIgnoreCase(accountType) ? "SB" : "CA") + (1000 + (int)(Math.random() * 9000));

        Account account = new Account();
        account.setAccountNumber(accNo);
        account.setCustomer(customer);
        account.setAccountType(accountType.toUpperCase());
        account.setBalance(initialDeposit);

        if ("SAVINGS".equalsIgnoreCase(accountType)) {
            account.setInterestRate(4.5);
            account.setOverdraftLimit(0.0);
        } else {
            account.setInterestRate(0.0);
            account.setOverdraftLimit(25000.0);
        }

        Account saved = accountRepository.save(account);

        if (initialDeposit > 0) {
            transactionService.recordTransaction(accNo, "CREDIT", initialDeposit, "DEPOSIT", "Account Opening Initial Deposit");
        }

        return saved;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with Number: " + accountNumber));
    }

    public List<Account> getAccountsByCustomerId(Integer customerId) {
        return accountRepository.findByCustomerCustomerId(customerId);
    }

    @Transactional
    public Transaction deposit(String accountNumber, Double amount, String description) {
        if (amount == null || amount <= 0) {
            throw new InvalidOperationException("Deposit amount must be strictly positive.");
        }
        Account acc = getAccountByNumber(accountNumber);
        acc.setBalance(acc.getBalance() + amount);
        accountRepository.save(acc);

        return transactionService.recordTransaction(accountNumber, "CREDIT", amount, "DEPOSIT", description);
    }

    @Transactional
    public Transaction withdraw(String accountNumber, Double amount, String description) {
        if (amount == null || amount <= 0) {
            throw new InvalidOperationException("Withdrawal amount must be strictly positive.");
        }
        Account acc = getAccountByNumber(accountNumber);

        if ("SAVINGS".equalsIgnoreCase(acc.getAccountType())) {
            if (acc.getBalance() - amount < 1000.0) {
                throw new InsufficientBalanceException("Withdrawal denied: Savings accounts must maintain a minimum balance of ₹1,000.");
            }
        } else {
            if (acc.getBalance() + acc.getOverdraftLimit() < amount) {
                throw new InsufficientBalanceException("Withdrawal denied: Amount exceeds available balance and overdraft limit.");
            }
        }

        acc.setBalance(acc.getBalance() - amount);
        accountRepository.save(acc);

        // Update spending limit tracking if present
        Optional<SpendingLimit> limitOpt = spendingLimitRepository.findByAccountNumber(accountNumber);
        limitOpt.ifPresent(limit -> {
            limit.setCurrentSpending(limit.getCurrentSpending() + amount);
            spendingLimitRepository.save(limit);
        });

        return transactionService.recordTransaction(accountNumber, "DEBIT", amount, "WITHDRAWAL", description);
    }

    @Transactional
    public Transaction transfer(String fromAccNo, String toAccNo, Double amount, String description) {
        if (fromAccNo.equalsIgnoreCase(toAccNo)) {
            throw new InvalidOperationException("Source and target accounts must be different.");
        }
        withdraw(fromAccNo, amount, "Transfer to " + toAccNo + ": " + (description != null ? description : ""));
        return deposit(toAccNo, amount, "Transfer from " + fromAccNo + ": " + (description != null ? description : ""));
    }
}
