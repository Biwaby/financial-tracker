package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.create.WalletTransactionCreateDto;
import com.biwaby.financialtracker.dto.update.WalletTransactionUpdateDto;
import com.biwaby.financialtracker.entity.WalletTransaction;

import java.util.List;

public interface WalletTransactionService {

    WalletTransaction save(WalletTransaction walletTransaction);
    WalletTransaction create(Long walletId, Long categoryId, WalletTransactionCreateDto dto);
    WalletTransaction getById(Long id);
    List<WalletTransaction> getAll(Long walletId, Integer pageSize, Integer pageNumber);
    WalletTransaction update(Long id, Long categoryId, WalletTransactionUpdateDto dto);
    void deleteById(Long id);
}
