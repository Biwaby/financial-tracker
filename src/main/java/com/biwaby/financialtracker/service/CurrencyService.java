package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.update.CurrencyUpdateDto;
import com.biwaby.financialtracker.entity.Currency;

import java.util.List;

public interface CurrencyService {

    Currency save(Currency currency);
    Currency create(Currency currency);
    Currency getById(Long id);
    Currency getByCode(String code);
    List<Currency> getAll();
    Currency update(Long id, CurrencyUpdateDto dto);
    void deleteById(Long id);
}
