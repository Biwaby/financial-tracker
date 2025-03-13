package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.entity.Currency;

import java.util.List;

public interface CurrencyService {

    Currency add(Currency currency);
    Currency getById(Long id);
    List<Currency> getAll();
    Currency edit(Long id, Currency currency);
    void delete(Long id);
}
