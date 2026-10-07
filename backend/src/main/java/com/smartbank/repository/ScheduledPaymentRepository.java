package com.smartbank.repository;

import com.smartbank.entity.ScheduledPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduledPaymentRepository extends JpaRepository<ScheduledPayment, Integer> {
    List<ScheduledPayment> findByAccountNumber(String accountNumber);
}
