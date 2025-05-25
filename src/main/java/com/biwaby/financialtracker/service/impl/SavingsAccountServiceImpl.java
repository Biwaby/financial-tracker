package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.create.SavingsAccountCreateDto;
import com.biwaby.financialtracker.dto.create.SavingsTransactionCreateDto;
import com.biwaby.financialtracker.dto.update.SavingsAccountUpdateDto;
import com.biwaby.financialtracker.entity.Currency;
import com.biwaby.financialtracker.entity.SavingsAccount;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.enums.SavingsAccountStatus;
import com.biwaby.financialtracker.enums.SavingsTransactionType;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.SavingsAccountRepository;
import com.biwaby.financialtracker.service.CurrencyService;
import com.biwaby.financialtracker.service.SavingsAccountService;
import com.biwaby.financialtracker.service.SavingsTransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavingsAccountServiceImpl implements SavingsAccountService {

    private final SavingsAccountRepository savingsAccountRepository;
    private final CurrencyService currencyService;
    private final SavingsTransactionService savingsTransactionService;

    @Override
    @Transactional
    public SavingsAccount save(SavingsAccount savingsAccount) {
        return savingsAccountRepository.save(savingsAccount);
    }

    @Override
    @Transactional
    public SavingsAccount create(User user, SavingsAccountCreateDto dto) {
        if (savingsAccountRepository.existsByNameAndUser(dto.getName(), user)) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Savings account with name <%s> already exists".formatted(dto.getName())
            );
        }

        SavingsAccount accountToCreate = new SavingsAccount();
        accountToCreate.setUser(user);
        accountToCreate.setName(dto.getName());

        Currency accountCurrency = currencyService.getByLetterCode(dto.getCurrencyLetterCode().toUpperCase());
        accountToCreate.setCurrency(accountCurrency);

        accountToCreate.setTargetAmount(dto.getTargetAmount());

        if (dto.getDeadlineDate() != null && !dto.getDeadlineDate().trim().isEmpty()) {
            accountToCreate.setDeadlineDate(LocalDate.parse(dto.getDeadlineDate()));
        }

        accountToCreate.setStatus(SavingsAccountStatus.ACTIVE);
        accountCurrency.getSavingsAccountsWithCurrency().add(accountToCreate);
        accountToCreate.getUser().getSavingsAccounts().add(accountToCreate);
        return save(accountToCreate);
    }

    @Override
    public SavingsAccount getById(User user, Long id) {
        return savingsAccountRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Savings account with id <%s> is not found or not allowed".formatted(id)
                )
        );
    }

    @Override
    public List<SavingsAccount> getAll(User user) {
        return savingsAccountRepository.findAllByUser(user);
    }

    @Override
    @Transactional
    public SavingsAccount deposit(User user, Long id, BigDecimal amount) {
        SavingsAccount account = getById(user, id);
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            account.setCurrentAmount(account.getCurrentAmount().add(amount));
            savingsTransactionService.create(
                    user,
                    account,
                    new SavingsTransactionCreateDto(
                            "Deposit",
                            SavingsTransactionType.INCOME.getDisplayName(),
                            amount,
                            LocalDateTime.now().toString(),
                            "Depositing funds to the savings account"
                    )
            );
        } else {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Deposit <amount> cannot be null, zero or negative"
            );
        }
        return save(account);
    }

    @Override
    @Transactional
    public SavingsAccount update(User user, Long id, SavingsAccountUpdateDto dto) {
        SavingsAccount accountToUpdate = getById(user, id);

        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            if (!savingsAccountRepository.existsByNameAndUser(dto.getName(), user)) {
                accountToUpdate.setName(dto.getName());
            } else {
                throw new ResponseException(
                        HttpStatus.BAD_REQUEST.value(),
                        "Savings account with name <%s> already exists".formatted(dto.getName())
                );
            }
        }
        if (dto.getTargetAmount() != null) {
            if (dto.getTargetAmount().compareTo(BigDecimal.ZERO) > 0) {
                if (dto.getTargetAmount().compareTo(accountToUpdate.getCurrentAmount()) <= 0) {
                    accountToUpdate.setStatus(SavingsAccountStatus.COMPLETED);
                } else {
                    accountToUpdate.setStatus(SavingsAccountStatus.ACTIVE);
                }
                accountToUpdate.setTargetAmount(dto.getTargetAmount());
            } else {
                throw new ResponseException(
                        HttpStatus.BAD_REQUEST.value(),
                        "Deposit <targetAmount> cannot be null, zero or negative"
                );
            }
        }
        if (dto.getCurrentAmount() != null) {
            if (dto.getCurrentAmount().compareTo(BigDecimal.ZERO) >= 0) {
                BigDecimal diff = dto.getCurrentAmount().subtract(accountToUpdate.getCurrentAmount());
                SavingsTransactionType transactionType = diff.compareTo(BigDecimal.ZERO) > 0 ?
                        SavingsTransactionType.INCOME :
                        SavingsTransactionType.EXPENSE;
                BigDecimal oldCurrentAmount = accountToUpdate.getCurrentAmount();
                accountToUpdate.setCurrentAmount(dto.getCurrentAmount());
                savingsTransactionService.create(
                        user,
                        accountToUpdate,
                        new SavingsTransactionCreateDto(
                                "Updating current amount",
                                transactionType.getDisplayName(),
                                diff.abs(),
                                LocalDateTime.now().toString(),
                                "Manually updating the savings account current amount from <%s %s> to <%s %s>"
                                        .formatted(oldCurrentAmount, accountToUpdate.getCurrency().getLetterCode(), dto.getCurrentAmount(), accountToUpdate.getCurrency().getLetterCode())
                        )
                );

                if (accountToUpdate.getCurrentAmount().compareTo(accountToUpdate.getTargetAmount()) >= 0) {
                    accountToUpdate.setStatus(SavingsAccountStatus.COMPLETED);
                } else {
                    accountToUpdate.setStatus(SavingsAccountStatus.ACTIVE);
                }
            } else {
                throw new ResponseException(
                        HttpStatus.BAD_REQUEST.value(),
                        "Deposit <currentAmount> cannot be null or negative"
                );
            }
        }
        if (dto.getDeadlineDate() != null && !dto.getDeadlineDate().trim().isEmpty()) {
            accountToUpdate.setDeadlineDate(LocalDate.parse(dto.getDeadlineDate()));
        }
        if (dto.getStatus() != null && !dto.getStatus().trim().isEmpty()) {
            accountToUpdate.setStatus(SavingsAccountStatus.getStatusByValue(dto.getStatus()));
        }
        return save(accountToUpdate);
    }

    // TODO: changeCurrency method in SavingsAccountService
    @Override
    @Transactional
    public SavingsAccount changeWalletCurrency(User user, Long id, Currency currency) {
        return null;
    }

    @Override
    @Transactional
    public void deleteById(User user, Long id) {
        SavingsAccount accountToDelete = getById(user, id);
        Currency holderCurrency = accountToDelete.getCurrency();
        holderCurrency.getSavingsAccountsWithCurrency().remove(accountToDelete);
        savingsAccountRepository.delete(accountToDelete);
    }
}
