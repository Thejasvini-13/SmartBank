package com.smartbank.repository;

import com.smartbank.entity.SavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Integer> {
    List<SavingsGoal> findByCustomerId(Integer customerId);
}
