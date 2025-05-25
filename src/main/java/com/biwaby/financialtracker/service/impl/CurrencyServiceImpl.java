package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.update.CurrencyUpdateDto;
import com.biwaby.financialtracker.entity.Currency;
import com.biwaby.financialtracker.entity.SavingsAccount;
import com.biwaby.financialtracker.entity.Wallet;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.CurrencyRepository;
import com.biwaby.financialtracker.repository.SavingsAccountRepository;
import com.biwaby.financialtracker.repository.WalletRepository;
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
    private final WalletRepository walletRepository;
    private final SavingsAccountRepository savingsAccountRepository;

    @Override
    @Transactional
    public Currency save(Currency currency) {
        return currencyRepository.save(currency);
    }

    @Override
    @Transactional
    public Currency create(Currency currency) {
        String currencyLetterCode = currency.getLetterCode().toUpperCase();
        currency.setLetterCode(currencyLetterCode);

        if (currencyRepository.existsByCode(currency.getCode())) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Currency with code <%s> already exists".formatted(currency.getCode())
            );
        }
        if (currencyRepository.existsByLetterCode(currency.getLetterCode())) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Currency with letter code <%s> already exists".formatted(currency.getLetterCode())
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
    public Currency getByLetterCode(String letterCode) {
        return currencyRepository.findByLetterCode(letterCode).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Currency with letter code <%s> is not found".formatted(letterCode)
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
            currencyToUpdate.setCode(dto.getCode());
        }
        if (dto.getLetterCode() != null && !dto.getLetterCode().trim().isEmpty()) {
            String currencyLetterCode = dto.getLetterCode().toUpperCase();
            dto.setLetterCode(currencyLetterCode);

            if (dto.getLetterCode().equals("NON")) {
                throw new ResponseException(
                        HttpStatus.BAD_REQUEST.value(),
                        "Currency with letter code <%s> cannot be updated".formatted(dto.getLetterCode())
                );
            }
            if (currencyRepository.existsByLetterCode(dto.getLetterCode())) {
                throw new ResponseException(
                        HttpStatus.BAD_REQUEST.value(),
                        "Currency with letter code <%s> already exists".formatted(dto.getLetterCode())
                );
            }
            currencyToUpdate.setLetterCode(dto.getLetterCode());
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
        Currency nonCurrency = getByLetterCode("NON");
        List<Wallet> walletsWithDeletedCurrency = currencyToDelete.getWalletsWithCurrency();
        List<SavingsAccount> savingsAccountsWithDeletedCurrency = currencyToDelete.getSavingsAccountsWithCurrency();

        if (currencyToDelete.equals(nonCurrency)) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Currency with id <%s> cannot be deleted".formatted(id)
            );
        }

        setNonCurrencyForWallets(walletsWithDeletedCurrency, nonCurrency);
        setNonCurrencyForSavingsAccounts(savingsAccountsWithDeletedCurrency, nonCurrency);
        currencyRepository.delete(currencyToDelete);
    }

    private void setNonCurrencyForWallets(List<Wallet> wallets, Currency nonCurrency) {
        if (!wallets.isEmpty()) {
            wallets.forEach(wallet -> wallet.setCurrency(nonCurrency));
            walletRepository.saveAll(wallets);
        }
    }

    private void setNonCurrencyForSavingsAccounts(List<SavingsAccount> savingsAccounts, Currency nonCurrency) {
        if (!savingsAccounts.isEmpty()) {
            savingsAccounts.forEach(savingsAccount -> savingsAccount.setCurrency(nonCurrency));
            savingsAccountRepository.saveAll(savingsAccounts);
        }
    }
}
