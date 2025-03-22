package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.CurrencyUpdateDto;
import com.biwaby.financialtracker.entity.Currency;

import java.util.List;

public interface CurrencyService {

    Currency save(Currency currency);
    Currency add(Currency currency);
    Currency getById(Long id);
    List<Currency> getAll();
    Currency edit(Long id, CurrencyUpdateDto dto);
    void delete(Long id);
}
