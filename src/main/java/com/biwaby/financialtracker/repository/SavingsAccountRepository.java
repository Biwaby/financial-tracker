package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
}
