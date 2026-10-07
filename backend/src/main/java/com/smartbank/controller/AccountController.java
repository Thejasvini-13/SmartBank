package com.smartbank.controller;

import com.smartbank.entity.Account;
import com.smartbank.entity.Transaction;
import com.smartbank.dto.DepositRequest;
import com.smartbank.dto.WithdrawRequest;
import com.smartbank.dto.TransferRequest;
import com.smartbank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestParam Integer customerId,
                                                 @RequestParam String accountType,
                                                 @RequestParam Double initialDeposit) {
        Account created = accountService.createAccount(customerId, accountType, initialDeposit);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable String id) {
        return ResponseEntity.ok(accountService.getAccountByNumber(id));
    }

    @GetMapping("/accounts/customer/{customerId}")
    public ResponseEntity<List<Account>> getAccountsByCustomerId(@PathVariable Integer customerId) {
        return ResponseEntity.ok(accountService.getAccountsByCustomerId(customerId));
    }

    @GetMapping("/accounts/{id}/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable String id) {
        Account acc = accountService.getAccountByNumber(id);
        Map<String, Object> map = new HashMap<>();
        map.put("accountNumber", acc.getAccountNumber());
        map.put("balance", acc.getBalance());
        map.put("accountType", acc.getAccountType());
        map.put("status", acc.getStatus());
        return ResponseEntity.ok(map);
    }

    @PostMapping("/accounts/{id}/deposit")
    public ResponseEntity<Transaction> deposit(@PathVariable String id, @RequestBody DepositRequest req) {
        Transaction tx = accountService.deposit(id, req.getAmount(), req.getDescription());
        return ResponseEntity.ok(tx);
    }

    @PostMapping("/accounts/{id}/withdraw")
    public ResponseEntity<Transaction> withdraw(@PathVariable String id, @RequestBody WithdrawRequest req) {
        Transaction tx = accountService.withdraw(id, req.getAmount(), req.getDescription());
        return ResponseEntity.ok(tx);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestBody TransferRequest req) {
        Transaction tx = accountService.transfer(req.getFromAccountId(), req.getToAccountId(), req.getAmount(), req.getDescription());
        return ResponseEntity.ok(tx);
    }
}
