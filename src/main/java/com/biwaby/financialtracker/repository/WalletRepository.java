package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Boolean existsByNameAndUser(String walletName, User user);
    Optional<Wallet> findByIdAndUser(Long walletId, User user);
    List<Wallet> findAllByUser(User user);
}
