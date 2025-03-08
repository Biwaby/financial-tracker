package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Integer> {
}
