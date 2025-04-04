package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.create.WalletCreateDto;
import com.biwaby.financialtracker.dto.update.WalletUpdateDto;
import com.biwaby.financialtracker.entity.Wallet;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    Wallet save(Wallet wallet);
    Wallet create(WalletCreateDto dto);
    Wallet getById(Long id);
    List<Wallet> getAll();
    Wallet deposit(Long id, Long categoryId, BigDecimal amount);
    Wallet withdraw(Long id, Long categoryId, BigDecimal amount);
    Pair<Wallet, Wallet> transfer(Long senderWalletId, Long targetWalletId, BigDecimal amount);
    Wallet update(Long id, WalletUpdateDto dto);
    Wallet changeCurrency(Long id, String currencyCode);
    void deleteById(Long id);
}
