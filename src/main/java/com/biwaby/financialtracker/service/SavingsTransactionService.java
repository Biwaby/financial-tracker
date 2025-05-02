package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.create.SavingsTransactionCreateDto;
import com.biwaby.financialtracker.dto.update.SavingsTransactionUpdateDto;
import com.biwaby.financialtracker.entity.SavingsAccount;
import com.biwaby.financialtracker.entity.SavingsTransaction;
import com.biwaby.financialtracker.entity.User;

import java.util.List;

public interface SavingsTransactionService {

    SavingsTransaction save(SavingsTransaction savingsTransaction);
    SavingsTransaction create(User user, SavingsAccount savingsAccount, SavingsTransactionCreateDto dto);
    SavingsTransaction getById(User user, Long id);
    List<SavingsTransaction> getAll(User user, SavingsAccount savingsAccount, Integer pageSize, Integer pageNumber);
    SavingsTransaction update(User user, Long id, SavingsTransactionUpdateDto dto);
    void deleteById(User user, Long id);
}
