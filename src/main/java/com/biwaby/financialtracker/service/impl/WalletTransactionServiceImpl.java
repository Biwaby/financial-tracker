package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.create.WalletTransactionCreateDto;
import com.biwaby.financialtracker.dto.update.WalletTransactionUpdateDto;
import com.biwaby.financialtracker.entity.Category;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.entity.Wallet;
import com.biwaby.financialtracker.entity.WalletTransaction;
import com.biwaby.financialtracker.enums.WalletTransactionType;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.WalletTransactionRepository;
import com.biwaby.financialtracker.service.WalletTransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;

    @Override
    @Transactional
    public WalletTransaction save(WalletTransaction walletTransaction) {
        return walletTransactionRepository.save(walletTransaction);
    }

    @Override
    @Transactional
    public WalletTransaction create(User user, Wallet wallet, Category category, WalletTransactionCreateDto dto) {
        WalletTransaction transactionToCreate = new WalletTransaction();

        transactionToCreate.setUser(user);
        transactionToCreate.setWallet(wallet);
        transactionToCreate.setCategory(category);
        transactionToCreate.setName(dto.getName());
        transactionToCreate.setType(WalletTransactionType.getTypeByValue(dto.getType()));
        transactionToCreate.setAmount(dto.getAmount());

        if (dto.getTransactionDate() != null && !dto.getTransactionDate().trim().isEmpty()) {
            transactionToCreate.setTransactionDate(LocalDateTime.parse(dto.getTransactionDate()));
        } else {
            transactionToCreate.setTransactionDate(LocalDateTime.now());
        }
        if (dto.getDescription() != null && !dto.getDescription().trim().isEmpty()) {
            transactionToCreate.setDescription(dto.getDescription());
        }

        category.getWalletsTransactionsWithCategory().add(transactionToCreate);

        return save(transactionToCreate);
    }

    @Override
    public WalletTransaction getById(User user, Long id) {
        return walletTransactionRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Wallet transaction with id <%s> is not found or not allowed".formatted(id)
                )
        );
    }

    @Override
    public List<WalletTransaction> getAll(User user, Wallet wallet, Integer pageSize, Integer pageNumber) {
        return walletTransactionRepository.findAllByUserAndWallet(user, wallet, PageRequest.of(pageNumber, pageSize))
                .get()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WalletTransaction update(User user, Long id, Category category, WalletTransactionUpdateDto dto) {
        WalletTransaction transactionToUpdate = getById(user, id);
        if (category != null) {
            transactionToUpdate.setCategory(category);
        }
        if (dto != null) {
            if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
                transactionToUpdate.setName(dto.getName());
            }
            if (dto.getType() != null && !dto.getType().trim().isEmpty()) {
                transactionToUpdate.setType(WalletTransactionType.getTypeByValue(dto.getType()));
            }
            if (dto.getAmount() != null) {
                if (dto.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                    throw new ResponseException(
                            HttpStatus.BAD_REQUEST.value(),
                            "<amount> must be equal or greater than zero"
                    );
                }
                transactionToUpdate.setAmount(dto.getAmount());
            }
            if (dto.getTransactionDate() != null && !dto.getTransactionDate().trim().isEmpty()) {
                transactionToUpdate.setTransactionDate(LocalDateTime.parse(dto.getTransactionDate(), DateTimeFormatter.ISO_DATE_TIME));
            }
            if (dto.getDescription() != null && !dto.getDescription().trim().isEmpty()) {
                transactionToUpdate.setDescription(dto.getDescription());
            }
        }
        return save(transactionToUpdate);
    }

    @Override
    @Transactional
    public void deleteById(User user, Long id) {
        WalletTransaction transactionToDelete = getById(user, id);
        walletTransactionRepository.delete(transactionToDelete);
    }
}
