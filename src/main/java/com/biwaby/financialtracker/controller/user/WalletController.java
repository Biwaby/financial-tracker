package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.create.WalletCreateDto;
import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.dto.update.WalletUpdateDto;
import com.biwaby.financialtracker.entity.Category;
import com.biwaby.financialtracker.entity.Currency;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.entity.Wallet;
import com.biwaby.financialtracker.service.CategoryService;
import com.biwaby.financialtracker.service.CurrencyService;
import com.biwaby.financialtracker.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final CategoryService categoryService;
    private final CurrencyService currencyService;

    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> createWallet(
            @RequestBody @Valid WalletCreateDto dto
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectResponse responseBody = new ObjectResponse(
                "Wallet created successfully",
                HttpStatus.OK.toString(),
                walletService.create(user, dto)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<ObjectResponse> getById(
            @RequestParam Long id
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectResponse responseBody = new ObjectResponse(
                "Wallet with id <%s>".formatted(id),
                HttpStatus.OK.toString(),
                walletService.getById(user, id)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ObjectListResponse> getAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectListResponse responseBody = new ObjectListResponse(
                "Wallets list",
                HttpStatus.OK.toString(),
                walletService.getAll(user).stream()
                        .map(wallet -> (Object) wallet)
                        .collect(Collectors.toList())
        );
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/deposit")
    public ResponseEntity<ObjectResponse> depositById(
            @RequestParam Long id,
            @RequestParam Long categoryId,
            @RequestParam BigDecimal amount
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category category = categoryService.getById(user, categoryId);
        Wallet wallet = walletService.deposit(user, id, category, amount);
        ObjectResponse responseBody = new ObjectResponse(
                "The amount of <%s %s> has been successfully deposited to the wallet (with id <%s>) balance".formatted(amount, wallet.getCurrency().getCode(), id),
                HttpStatus.OK.toString(),
                wallet
        );
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ObjectResponse> withdrawById(
            @RequestParam Long id,
            @RequestParam Long categoryId,
            @RequestParam BigDecimal amount
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category category = categoryService.getById(user, categoryId);
        Wallet wallet = walletService.withdraw(user, id, category, amount);
        ObjectResponse responseBody = new ObjectResponse(
                "The amount of <%s %s> has been successfully withdrawn from the wallet (with id <%s>) balance".formatted(amount, wallet.getCurrency().getCode(), id),
                HttpStatus.OK.toString(),
                wallet
        );
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/transfer")
    public ResponseEntity<ObjectResponse> transfer(
            @RequestParam Long senderWalletId,
            @RequestParam Long targetWalletId,
            @RequestParam BigDecimal amount
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pair<Wallet, Wallet> wallets = walletService.transfer(user, senderWalletId, targetWalletId, amount);
        ObjectResponse responseBody = new ObjectResponse(
                "The amount of <%s %s> has been successfully transferred from the wallet (with id <%s>) to the wallet (with id <%s>)"
                        .formatted(amount, wallets.getFirst().getCurrency().getCode(), senderWalletId, targetWalletId),
                HttpStatus.OK.toString(),
                wallets
        );
        return ResponseEntity.ok(responseBody);
    }

    @PatchMapping("/update")
    public ResponseEntity<ObjectResponse> updateById(
            @RequestParam Long id,
            @RequestBody @Valid WalletUpdateDto dto
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectResponse responseBody = new ObjectResponse(
                "Wallet with id <%s> has been successfully edited".formatted(id),
                HttpStatus.OK.toString(),
                walletService.update(user, id, dto)
        );
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/change-currency")
    public ResponseEntity<ObjectResponse> changeCurrency(
            @RequestParam Long id,
            @RequestParam String currencyCode
    ) {
        Currency currency = currencyService.getByCode(currencyCode);
        ObjectResponse responseBody = new ObjectResponse(
                "Currency for wallet with id <%s> has been successfully changed".formatted(id),
                HttpStatus.OK.toString(),
                walletService.changeWalletCurrency(id, currency)
        );
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ObjectResponse> deleteById(
            @RequestParam Long id
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Wallet deletedWallet = walletService.getById(user, id);
        walletService.deleteById(user, id);
        ObjectResponse responseBody = new ObjectResponse(
                "Wallet with id <%s> has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                deletedWallet
        );
        return ResponseEntity.ok(responseBody);
    }
}
