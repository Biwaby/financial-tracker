package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.create.WalletCreateDto;
import com.biwaby.financialtracker.dto.create.WalletTransactionCreateDto;
import com.biwaby.financialtracker.dto.update.WalletUpdateDto;
import com.biwaby.financialtracker.entity.*;
import com.biwaby.financialtracker.enums.WalletTransactionType;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.WalletRepository;
import com.biwaby.financialtracker.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final CurrencyService currencyService;
    private final WalletTransactionService walletTransactionService;
    private final LimitService limitService;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public Wallet create(User user, WalletCreateDto dto) {
        if (walletRepository.existsByNameAndUser(dto.getName(), user)) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Wallet with name <%s> already exists>".formatted(dto.getName())
            );
        }

        Wallet walletToCreate = new Wallet();
        walletToCreate.setUser(user);
        walletToCreate.setName(dto.getName());
        walletToCreate.setCurrency(currencyService.getByLetterCode(dto.getCurrencyLetterCode().toUpperCase()));

        Currency walletCurrency = walletToCreate.getCurrency();
        walletCurrency.getWalletsWithCurrency().add(walletToCreate);

        return save(walletToCreate);
    }

    @Override
    public Wallet getById(User user, Long id) {
        return walletRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Wallet with id <%s> is not found or not allowed".formatted(id)
                )
        );
    }

    @Override
    public List<Wallet> getAll(User user) {
        return walletRepository.findAllByUser(user);
    }

    @Override
    @Transactional
    public Wallet deposit(User user, Long id, Category category, BigDecimal amount) {
        Wallet wallet = getById(user, id);
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal currentBalance = wallet.getBalance();
            wallet.setBalance(currentBalance.add(amount));
            walletTransactionService.create(
                    user,
                    wallet,
                    category,
                    new WalletTransactionCreateDto(
                            "Deposit",
                            WalletTransactionType.INCOME.getDisplayName(),
                            amount,
                            LocalDateTime.now().toString(),
                            "Depositing funds to the wallet"
                    )
            );
        } else {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Deposit <amount> cannot be null, zero or negative"
            );
        }
        return save(wallet);
    }

    @Override
    @Transactional
    public Wallet withdraw(User user, Long id, Category category, BigDecimal amount) {
        Wallet wallet = getById(user, id);
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal currentBalance = wallet.getBalance();
            if (amount.compareTo(currentBalance) > 0) {
                throw new ResponseException(
                        HttpStatus.FORBIDDEN.value(),
                        "Not enough balance to withdraw <%s> <%s>".formatted(amount, wallet.getCurrency().getCode())
                );
            }
            wallet.setBalance(currentBalance.subtract(amount));
            walletTransactionService.create(
                    user,
                    wallet,
                    category,
                    new WalletTransactionCreateDto(
                            "Withdraw",
                            WalletTransactionType.EXPENSE.getDisplayName(),
                            amount,
                            LocalDateTime.now().toString(),
                            "Withdrawing funds from the wallet"
                    )
            );

            if (limitService.existsByWallet(wallet)) {
                limitService.updateForWallet(user, wallet, amount);
            }
        } else {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Withdrawal <amount> cannot be null, zero or negative"
            );
        }
        return save(wallet);
    }

    @Override
    @Transactional
    public Pair<Wallet, Wallet> transfer(User user, Long senderWalletId, Long targetWalletId, BigDecimal amount) {
        if (senderWalletId.equals(targetWalletId)) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Cannot transfer into same wallet as sender wallet"
            );
        }

        Wallet senderWallet = getById(user, senderWalletId);
        Wallet targetWallet = getById(user, targetWalletId);

        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal senderCurrentBalance = senderWallet.getBalance();

            if (amount.compareTo(senderCurrentBalance) > 0) {
                throw new ResponseException(
                        HttpStatus.FORBIDDEN.value(),
                        "Not enough balance to transfer <%s %s>".formatted(amount, senderWallet.getCurrency().getCode())
                );
            }
            if (!senderWallet.getCurrency().equals(targetWallet.getCurrency())) {
                throw new ResponseException(
                        HttpStatus.BAD_REQUEST.value(),
                        "The currency of the sender's wallet and target's wallet do not match"
                );
            }

            senderWallet.setBalance(senderCurrentBalance.subtract(amount));
            targetWallet.setBalance(targetWallet.getBalance().add(amount));
            walletTransactionService.create(
                    user,
                    senderWallet,
                    categoryService.getByName(user, "Service"),
                    new WalletTransactionCreateDto(
                            "Transfer to wallet <%s>".formatted(targetWallet.getName()),
                            WalletTransactionType.TRANSFER_EXPENSE.getDisplayName(),
                            amount,
                            LocalDateTime.now().toString(),
                            "Transfer funds to the wallet with name <%s>".formatted(targetWallet.getName())
                    )
            );

            if (limitService.existsByWallet(senderWallet)) {
                limitService.updateForWallet(user, senderWallet, amount);
            }

            walletTransactionService.create(
                    user,
                    targetWallet,
                    categoryService.getByName(user, "Service"),
                    new WalletTransactionCreateDto(
                            "Transfer from wallet <%s>".formatted(senderWallet.getName()),
                            WalletTransactionType.TRANSFER_INCOME.getDisplayName(),
                            amount,
                            LocalDateTime.now().toString(),
                            "Receiving a transfer from wallet with name <%s>".formatted(senderWallet.getName())
                    )
            );
        } else {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Transfer <amount> cannot be null, zero or negative"
            );
        }
        return Pair.of(save(senderWallet), save(targetWallet));
    }

    @Override
    @Transactional
    public Wallet update(User user, Long id, WalletUpdateDto dto) {
        Wallet walletToUpdate = getById(user, id);

        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            if (!walletRepository.existsByNameAndUser(dto.getName(), user)) {
                walletToUpdate.setName(dto.getName());
            } else {
                throw new ResponseException(
                        HttpStatus.BAD_REQUEST.value(),
                        "Wallet with name <%s> already exists>".formatted(dto.getName())
                );
            }
        }
        if (dto.getBalance() != null && dto.getBalance().compareTo(BigDecimal.ZERO) >= 0) {
            BigDecimal diff = dto.getBalance().subtract(walletToUpdate.getBalance());
            WalletTransactionType transactionType = diff.compareTo(BigDecimal.ZERO) > 0 ?
                    WalletTransactionType.INCOME :
                    WalletTransactionType.EXPENSE;
            BigDecimal oldBalance = walletToUpdate.getBalance();
            walletToUpdate.setBalance(dto.getBalance());
            walletTransactionService.create(
                    user,
                    walletToUpdate,
                    categoryService.getByName(user, "Service"),
                    new WalletTransactionCreateDto(
                            "Updating balance",
                            transactionType.getDisplayName(),
                            diff.abs(),
                            LocalDateTime.now().toString(),
                            "Manually updating the wallet balance from <%s %s> to <%s %s>"
                                    .formatted(oldBalance, walletToUpdate.getCurrency().getLetterCode(), dto.getBalance(), walletToUpdate.getCurrency().getLetterCode())
                    )
            );

            if (transactionType.equals(WalletTransactionType.EXPENSE) && limitService.existsByWallet(walletToUpdate)) {
                limitService.updateForWallet(user, walletToUpdate, diff.abs());
            }
        } else {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "The wallet <balance> cannot be null or negative"
            );
        }
        return save(walletToUpdate);
    }

    // TODO: changeCurrency method in WalletService
    @Override
    public Wallet changeWalletCurrency(Long id, Currency currency) {
        return null;
    }

    @Override
    @Transactional
    public void deleteById(User user, Long id) {
        Wallet wallet = getById(user, id);
        walletRepository.delete(wallet);
    }
}
