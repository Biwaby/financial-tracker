package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.SavingsTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsTransactionRepository extends JpaRepository<SavingsTransaction, Long> {
}
