package com.smartbank.repository;

import com.smartbank.entity.SpendingLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpendingLimitRepository extends JpaRepository<SpendingLimit, Integer> {
    Optional<SpendingLimit> findByAccountNumber(String accountNumber);
}
