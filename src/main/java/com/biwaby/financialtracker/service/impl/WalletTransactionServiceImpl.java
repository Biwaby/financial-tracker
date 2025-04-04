package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.create.WalletTransactionCreateDto;
import com.biwaby.financialtracker.dto.update.WalletTransactionUpdateDto;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.entity.Wallet;
import com.biwaby.financialtracker.entity.WalletTransaction;
import com.biwaby.financialtracker.enums.WalletTransactionType;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.WalletTransactionRepository;
import com.biwaby.financialtracker.service.CategoryService;
import com.biwaby.financialtracker.service.UserService;
import com.biwaby.financialtracker.service.WalletService;
import com.biwaby.financialtracker.service.WalletTransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;
    private final UserService userService;
    private final WalletService walletService;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public WalletTransaction save(WalletTransaction walletTransaction) {
        return walletTransactionRepository.save(walletTransaction);
    }

    @Override
    @Transactional
    public WalletTransaction create(Long walletId, Long categoryId, WalletTransactionCreateDto dto) {
        WalletTransaction transactionToCreate = new WalletTransaction();

        transactionToCreate.setUser(userService.getCurrentUserEntity());
        transactionToCreate.setWallet(walletService.getById(walletId));
        transactionToCreate.setCategory(categoryService.getById(categoryId));
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
        return save(transactionToCreate);
    }

    @Override
    public WalletTransaction getById(Long id) {
        User user = userService.getCurrentUserEntity();
        return walletTransactionRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Wallet transaction with id <%s> is not found or not allowed".formatted(id)
                )
        );
    }

    @Override
    public List<WalletTransaction> getAll(Long walletId, Integer pageSize, Integer pageNumber) {
        User user = userService.getCurrentUserEntity();
        Wallet wallet = walletService.getById(walletId);
        return walletTransactionRepository.findAllByUserAndWallet(user, wallet, PageRequest.of(pageNumber, pageSize))
                .get()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WalletTransaction update(Long id, Long categoryId, WalletTransactionUpdateDto dto) {
        WalletTransaction transactionToUpdate = getById(id);
        if (categoryId != null) {
            transactionToUpdate.setCategory(categoryService.getById(categoryId));
        }
        if (dto != null) {
            if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
                transactionToUpdate.setName(dto.getName());
            }
            if (dto.getType() != null && !dto.getType().trim().isEmpty()) {
                transactionToUpdate.setType(WalletTransactionType.getTypeByValue(dto.getType()));
            }
            if (dto.getAmount() != null) {
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
    public void deleteById(Long id) {
        WalletTransaction transactionToDelete = getById(id);
        walletTransactionRepository.delete(transactionToDelete);
    }
}
