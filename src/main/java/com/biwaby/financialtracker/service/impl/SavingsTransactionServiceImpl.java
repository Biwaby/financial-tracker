package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.create.SavingsTransactionCreateDto;
import com.biwaby.financialtracker.dto.update.SavingsTransactionUpdateDto;
import com.biwaby.financialtracker.entity.SavingsAccount;
import com.biwaby.financialtracker.entity.SavingsTransaction;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.enums.SavingsTransactionType;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.SavingsTransactionRepository;
import com.biwaby.financialtracker.service.SavingsTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavingsTransactionServiceImpl implements SavingsTransactionService {

    private final SavingsTransactionRepository savingsTransactionRepository;

    @Override
    public SavingsTransaction save(SavingsTransaction savingsTransaction) {
        return savingsTransactionRepository.save(savingsTransaction);
    }

    @Override
    public SavingsTransaction create(User user, SavingsAccount savingsAccount, SavingsTransactionCreateDto dto) {
        SavingsTransaction transactionToCreate = new SavingsTransaction();

        transactionToCreate.setUser(user);
        transactionToCreate.setSavingsAccount(savingsAccount);
        transactionToCreate.setName(dto.getName());
        transactionToCreate.setType(SavingsTransactionType.getTypeByValue(dto.getType()));
        transactionToCreate.setAmount(dto.getAmount());

        if (dto.getTransactionDate() != null && !dto.getTransactionDate().trim().isEmpty()) {
            transactionToCreate.setTransactionDate(LocalDateTime.parse(dto.getTransactionDate()));
        } else {
            transactionToCreate.setTransactionDate(LocalDateTime.now());
        }
        if (dto.getDescription() != null && !dto.getDescription().trim().isEmpty()) {
            transactionToCreate.setDescription(dto.getDescription());
        }

        savingsAccount.getSavingsTransactions().add(transactionToCreate);
        transactionToCreate.getUser().getSavingsTransactions().add(transactionToCreate);
        return save(transactionToCreate);
    }

    @Override
    public SavingsTransaction getById(User user, Long id) {
        return savingsTransactionRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Savings transaction with id <%s> is not found or not allowed".formatted(id)
                )
        );
    }

    @Override
    public List<SavingsTransaction> getAll(User user, SavingsAccount savingsAccount, Integer pageSize, Integer pageNumber) {
        return savingsTransactionRepository.findAllByUserAndSavingsAccount(user, savingsAccount, PageRequest.of(pageNumber, pageSize))
                .get()
                .collect(Collectors.toList());
    }

    @Override
    public SavingsTransaction update(User user, Long id, SavingsTransactionUpdateDto dto) {
        SavingsTransaction transactionToUpdate = getById(user, id);

        if (dto != null) {
            if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
                transactionToUpdate.setName(dto.getName());
            }
            if (dto.getType() != null && !dto.getType().trim().isEmpty()) {
                transactionToUpdate.setType(SavingsTransactionType.getTypeByValue(dto.getType()));
            }
            if (dto.getAmount() != null) {
                if (dto.getAmount().compareTo(BigDecimal.ZERO) >= 0) {
                    transactionToUpdate.setAmount(dto.getAmount());
                } else {
                    throw new ResponseException(
                            HttpStatus.BAD_REQUEST.value(),
                            "The <amount> must be equal or greater than zero"
                    );
                }
            }
            if (dto.getTransactionDate() != null && !dto.getTransactionDate().trim().isEmpty()) {
                transactionToUpdate.setTransactionDate(LocalDateTime.parse(dto.getTransactionDate()));
            }
            if (dto.getDescription() != null && !dto.getDescription().trim().isEmpty()) {
                transactionToUpdate.setDescription(dto.getDescription());
            }
        }

        return save(transactionToUpdate);
    }

    @Override
    public void deleteById(User user, Long id) {
        SavingsTransaction transactionToDelete = getById(user, id);
        SavingsAccount holderAccount = transactionToDelete.getSavingsAccount();
        holderAccount.getSavingsTransactions().remove(transactionToDelete);
        savingsTransactionRepository.delete(transactionToDelete);
    }
}
