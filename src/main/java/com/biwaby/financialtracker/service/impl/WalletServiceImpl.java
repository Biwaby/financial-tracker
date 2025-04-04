package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.create.WalletCreateDto;
import com.biwaby.financialtracker.dto.update.WalletUpdateDto;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.entity.Wallet;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.WalletRepository;
import com.biwaby.financialtracker.service.CurrencyService;
import com.biwaby.financialtracker.service.UserService;
import com.biwaby.financialtracker.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserService userService;
    private final CurrencyService currencyService;

    @Override
    @Transactional
    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public Wallet create(WalletCreateDto dto) {
        User user = userService.getCurrentUserEntity();

        if (walletRepository.existsByNameAndUser(dto.getName(), user)) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Wallet with name <%s> already exists>".formatted(dto.getName())
            );
        }

        Wallet walletToCreate = new Wallet();
        walletToCreate.setUser(user);
        walletToCreate.setName(dto.getName());
        walletToCreate.setCurrency(currencyService.getByCode(dto.getCurrencyCode().toUpperCase()));
        return save(walletToCreate);
    }

    @Override
    public Wallet getById(Long id) {
        User user = userService.getCurrentUserEntity();
        return walletRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Wallet with id <%s> is not found or not allowed".formatted(id)
                )
        );
    }

    @Override
    public List<Wallet> getAll() {
        User user = userService.getCurrentUserEntity();
        return walletRepository.findAllByUser(user);
    }

    @Override
    @Transactional
    public Wallet deposit(Long id, Long categoryId, BigDecimal amount) {
        Wallet wallet = getById(id);
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal currentBalance = wallet.getBalance();
            wallet.setBalance(currentBalance.add(amount));
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
    public Wallet withdraw(Long id, Long categoryId, BigDecimal amount) {
        Wallet wallet = getById(id);
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal currentBalance = wallet.getBalance();
            if (amount.compareTo(currentBalance) > 0) {
                throw new ResponseException(
                        HttpStatus.FORBIDDEN.value(),
                        "Not enough balance to withdraw <%s> <%s>".formatted(amount, wallet.getCurrency().getCode())
                );
            }
            wallet.setBalance(currentBalance.subtract(amount));
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
    public Pair<Wallet, Wallet> transfer(Long senderWalletId, Long targetWalletId, BigDecimal amount) {
        Wallet senderWallet = getById(senderWalletId);
        Wallet targetWallet = getById(targetWalletId);

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
    public Wallet update(Long id, WalletUpdateDto dto) {
        Wallet walletToUpdate = getById(id);
        User user = userService.getCurrentUserEntity();

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
            walletToUpdate.setBalance(dto.getBalance());
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
    public Wallet changeCurrency(Long id, String currencyCode) {
        return null;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Wallet wallet = getById(id);
        walletRepository.delete(wallet);
    }
}
