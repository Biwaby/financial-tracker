package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.SavingsAccount;
import com.biwaby.financialtracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {

    Boolean existsByNameAndUser(String accountName, User user);
    Optional<SavingsAccount> findByIdAndUser(Long accountId, User user);
    List<SavingsAccount> findAllByUser(User user);
}
