package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.create.WalletCreateDto;
import com.biwaby.financialtracker.dto.update.WalletUpdateDto;
import com.biwaby.financialtracker.entity.Category;
import com.biwaby.financialtracker.entity.Currency;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.entity.Wallet;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    Wallet save(Wallet wallet);
    Wallet create(User user, WalletCreateDto dto);
    Wallet getById(User user, Long id);
    List<Wallet> getAll(User user);
    Wallet deposit(User user, Long id, Category category, BigDecimal amount);
    Wallet withdraw(User user, Long id, Category category, BigDecimal amount);
    Pair<Wallet, Wallet> transfer(User user, Long senderWalletId, Long targetWalletId, BigDecimal amount);
    Wallet update(User user, Long id, WalletUpdateDto dto);
    Wallet changeWalletCurrency(Long id, Currency currency);
    void deleteById(User user, Long id);
}
