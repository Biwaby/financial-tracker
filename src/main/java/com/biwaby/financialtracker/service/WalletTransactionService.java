package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.create.WalletTransactionCreateDto;
import com.biwaby.financialtracker.dto.update.WalletTransactionUpdateDto;
import com.biwaby.financialtracker.entity.Category;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.entity.Wallet;
import com.biwaby.financialtracker.entity.WalletTransaction;

import java.util.List;

public interface WalletTransactionService {

    WalletTransaction save(WalletTransaction walletTransaction);
    WalletTransaction create(User user, Wallet wallet, Category category, WalletTransactionCreateDto dto);
    WalletTransaction getById(User user, Long id);
    List<WalletTransaction> getAll(User user, Wallet wallet, Integer pageSize, Integer pageNumber);
    WalletTransaction update(User user, Long id, Category category, WalletTransactionUpdateDto dto);
    void deleteById(User user, Long id);
}
