package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.create.SavingsAccountCreateDto;
import com.biwaby.financialtracker.dto.update.SavingsAccountUpdateDto;
import com.biwaby.financialtracker.entity.Currency;
import com.biwaby.financialtracker.entity.SavingsAccount;
import com.biwaby.financialtracker.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface SavingsAccountService {

    SavingsAccount save(SavingsAccount savingsAccount);
    SavingsAccount create(User user, SavingsAccountCreateDto dto);
    SavingsAccount getById(User user, Long id);
    List<SavingsAccount> getAll(User user);
    SavingsAccount deposit(User user, Long id, BigDecimal amount);
    SavingsAccount update(User user, Long id, SavingsAccountUpdateDto dto);
    SavingsAccount changeWalletCurrency(User user, Long id, Currency currency);
    void deleteById(User user, Long id);
}
