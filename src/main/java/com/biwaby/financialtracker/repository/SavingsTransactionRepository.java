package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.SavingsTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsTransactionRepository extends JpaRepository<SavingsTransaction, Long> {
}
