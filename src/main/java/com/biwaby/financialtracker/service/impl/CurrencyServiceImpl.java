package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.update.CurrencyUpdateDto;
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
    public Currency create(Currency currency) {
        String currencyCode = currency.getCode().toUpperCase();
        currency.setCode(currencyCode);

        if (currencyRepository.existsByCode(currency.getCode())) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Currency with code <%s> already exists".formatted(currency.getCode())
            );
        }
        if (currencyRepository.existsByName(currency.getName())) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Currency with name <%s> already exists".formatted(currency.getName())
            );
        }
        return save(currency);
    }

    @Override
    public Currency getById(Long id) {
        return currencyRepository.findById(id).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Currency with id <%s> is not found".formatted(id)
                )
        );
    }

    @Override
    public Currency getByCode(String code) {
        return currencyRepository.findByCode(code).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Currency with code <%s> is not found".formatted(code)
                )
        );
    }

    @Override
    public List<Currency> getAll() {
        return currencyRepository.findAll();
    }

    @Override
    @Transactional
    public Currency update(Long id, CurrencyUpdateDto dto) {
        Currency currencyToUpdate = getById(id);

        if (dto.getCode() != null && !dto.getCode().trim().isEmpty()) {
            String currencyCode = dto.getCode().toUpperCase();
            dto.setCode(currencyCode);

            if (dto.getCode().equals("NON")) {
                throw new ResponseException(
                        HttpStatus.BAD_REQUEST.value(),
                        "Currency with code <%s> cannot be updated".formatted(currencyCode)
                );
            }
            if (currencyRepository.existsByCode(currencyCode)) {
                throw new ResponseException(
                        HttpStatus.BAD_REQUEST.value(),
                        "Currency with code <%s> already exists".formatted(dto.getCode())
                );
            }
            currencyToUpdate.setCode(dto.getCode());
        }
        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            if (dto.getName().equals("Missing currency")) {
                throw new ResponseException(
                        HttpStatus.BAD_REQUEST.value(),
                        "Currency with name <%s> cannot be updated".formatted(dto.getName())
                );
            }
            if (currencyRepository.existsByName(dto.getName())) {
                throw new ResponseException(
                        HttpStatus.BAD_REQUEST.value(),
                        "Currency with name <%s> already exists".formatted(dto.getName())
                );
            }
            currencyToUpdate.setName(dto.getName());
        }
        return save(currencyToUpdate);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Currency currencyToDelete = getById(id);
        if (currencyToDelete.getCode().equals("NON") || currencyToDelete.getName().equals("Missing currency")) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Currency with id <%s> cannot be deleted".formatted(id)
            );
        }
        currencyRepository.delete(currencyToDelete);
    }
}
