package com.smartbank.repository;

import com.smartbank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByAccountNumberOrderByTimestampDesc(String accountNumber);

    List<Transaction> findByIsSuspiciousTrueOrderByTimestampDesc();

    @Query("SELECT t FROM Transaction t WHERE " +
           "(:accountNumber IS NULL OR t.accountNumber = :accountNumber) AND " +
           "(:category IS NULL OR LOWER(t.category) = LOWER(:category)) AND " +
           "(:type IS NULL OR UPPER(t.type) = UPPER(:type)) AND " +
           "(:minAmount IS NULL OR t.amount >= :minAmount) AND " +
           "(:maxAmount IS NULL OR t.amount <= :maxAmount) " +
           "ORDER BY t.timestamp DESC")
    List<Transaction> filterTransactions(
            @Param("accountNumber") String accountNumber,
            @Param("category") String category,
            @Param("type") String type,
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount);
}
