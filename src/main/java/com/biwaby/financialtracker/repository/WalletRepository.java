package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {
}
