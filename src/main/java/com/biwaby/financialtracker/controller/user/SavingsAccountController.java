package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.create.SavingsAccountCreateDto;
import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.dto.update.SavingsAccountUpdateDto;
import com.biwaby.financialtracker.entity.Currency;
import com.biwaby.financialtracker.entity.SavingsAccount;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.service.CurrencyService;
import com.biwaby.financialtracker.service.SavingsAccountService;
import com.biwaby.financialtracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/savings-accounts")
@RequiredArgsConstructor
public class SavingsAccountController {

    private final SavingsAccountService savingsAccountService;
    private final CurrencyService currencyService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> createAccount(
            @RequestBody @Valid SavingsAccountCreateDto dto
    ) {
        User user = userService.getSelfEntity();
        ObjectResponse responseBody = new ObjectResponse(
                "Savings account created successfully",
                HttpStatus.OK.toString(),
                savingsAccountService.create(user, dto)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<ObjectResponse> getById(
            @RequestParam Long id
    ) {
        User user = userService.getSelfEntity();
        ObjectResponse responseBody = new ObjectResponse(
                "Savings account with id <%s>".formatted(id),
                HttpStatus.OK.toString(),
                savingsAccountService.getById(user, id)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ObjectListResponse> getAll() {
        User user = userService.getSelfEntity();
        ObjectListResponse responseBody = new ObjectListResponse(
                "Savings accounts list",
                HttpStatus.OK.toString(),
                savingsAccountService.getAll(user).stream()
                        .map(account -> (Object) account)
                        .collect(Collectors.toList())
        );
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/deposit")
    public ResponseEntity<ObjectResponse> depositById(
            @RequestParam Long id,
            @RequestParam BigDecimal amount
    ) {
        User user = userService.getSelfEntity();
        SavingsAccount account = savingsAccountService.getById(user, id);
        ObjectResponse responseBody = new ObjectResponse(
                "The amount of <%s %s> has been successfully deposited to the savings account (with id <%s>) balance".formatted(amount, account.getCurrency().getLetterCode(), id),
                HttpStatus.OK.toString(),
                savingsAccountService.deposit(user, id, amount)
        );
        return ResponseEntity.ok(responseBody);
    }

    @PatchMapping("/update")
    public ResponseEntity<ObjectResponse> updateById(
            @RequestParam Long id,
            @RequestBody @Valid SavingsAccountUpdateDto dto
    ) {
        User user = userService.getSelfEntity();
        ObjectResponse responseBody = new ObjectResponse(
                "Savings account with id <%s> has been successfully edited".formatted(id),
                HttpStatus.OK.toString(),
                savingsAccountService.update(user, id, dto)
        );
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/change-currency")
    public ResponseEntity<ObjectResponse> changeCurrency(
            @RequestParam Long id,
            @RequestParam String currencyCode
    ) {
        Currency currency = currencyService.getByCode(currencyCode);
        User user = userService.getSelfEntity();
        ObjectResponse responseBody = new ObjectResponse(
                "Currency for savings account with id <%s> has been successfully changed".formatted(id),
                HttpStatus.OK.toString(),
                savingsAccountService.changeWalletCurrency(user, id, currency)
        );
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ObjectResponse> deleteById(
            @RequestParam Long id
    ) {
        User user = userService.getSelfEntity();
        SavingsAccount deletedAccount = savingsAccountService.getById(user, id);
        savingsAccountService.deleteById(user, id);
        ObjectResponse responseBody = new ObjectResponse(
                "Savings account with id <%s> has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                deletedAccount
        );
        return ResponseEntity.ok(responseBody);
    }
}
