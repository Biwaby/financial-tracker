package com.biwaby.financialtracker.repository;

import com.biwaby.financialtracker.entity.Limit;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {

    Optional<Limit> findByIdAndUser(Long limitId, User user);
    List<Limit> findAllByUser(User user);
    Boolean existsByWallet(Wallet wallet);
    Optional<Limit> findByWalletAndUser(Wallet wallet, User user);
}
