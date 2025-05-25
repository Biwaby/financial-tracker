package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.SavingsAccount;
import com.biwaby.financialtracker.entity.SavingsTransaction;
import com.biwaby.financialtracker.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingsTransactionRepository extends JpaRepository<SavingsTransaction, Long> {

    Optional<SavingsTransaction> findByIdAndUser(Long id, User user);
    Page<SavingsTransaction> findAllByUserAndSavingsAccount(User user, SavingsAccount savingsAccount, Pageable pageable);
}
