package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.create.LimitCreateDto;
import com.biwaby.financialtracker.dto.update.LimitUpdateDto;
import com.biwaby.financialtracker.entity.Limit;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.entity.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface LimitService {

    Limit save(Limit limit);
    Limit create(User user, Wallet wallet, LimitCreateDto dto);
    Limit getById(User user, Long id);
    Limit getByWallet(User user, Wallet wallet);
    Boolean existsByWallet(Wallet wallet);
    List<Limit> getAll(User user);
    void updateForWallet(User user, Wallet wallet, BigDecimal amount);
    Limit update(User user, Long id, LimitUpdateDto dto);
    void deleteById(User user, Long id);
    void deleteByWallet(User user, Wallet wallet);
}
