package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.entity.Currency;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.CurrencyRepository;
import com.biwaby.financialtracker.service.CurrencyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Override
    @Transactional
    public Currency save(Currency currency) {
        return currencyRepository.save(currency);
    }

    @Override
    @Transactional
    public Currency add(Currency currency) {
        return save(currency);
    }

    @Override
    public Currency getById(Long id) {
        return currencyRepository.findById(id).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Currency with id %s is not found".formatted(id)
                )
        );
    }

    @Override
    public List<Currency> getAll() {
        return currencyRepository.findAll();
    }

    @Override
    @Transactional
    public Currency edit(Long id, Currency currency) {
        Currency currencyToEdit = getById(id);
        currencyToEdit.setCode(currency.getCode());
        currencyToEdit.setName(currency.getName());
        return save(currencyToEdit);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Currency currencyToDelete = getById(id);
        currencyRepository.delete(currencyToDelete);
    }
}
