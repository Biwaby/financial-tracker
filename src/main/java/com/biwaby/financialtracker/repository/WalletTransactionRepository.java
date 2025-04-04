package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.entity.Wallet;
import com.biwaby.financialtracker.entity.WalletTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    Optional<WalletTransaction> findByIdAndUser(Long id, User user);
    Page<WalletTransaction> findAllByUserAndWallet(User user, Wallet wallet, Pageable pageable);
}
