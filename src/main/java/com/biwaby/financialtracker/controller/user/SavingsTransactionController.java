package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.create.SavingsTransactionCreateDto;
import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.dto.update.SavingsTransactionUpdateDto;
import com.biwaby.financialtracker.entity.SavingsAccount;
import com.biwaby.financialtracker.entity.SavingsTransaction;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.service.SavingsAccountService;
import com.biwaby.financialtracker.service.SavingsTransactionService;
import com.biwaby.financialtracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/savings-transactions")
@RequiredArgsConstructor
public class SavingsTransactionController {

    private final SavingsTransactionService savingsTransactionService;
    private final SavingsAccountService savingsAccountService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> create(
            @RequestParam Long savingsAccountId,
            @RequestBody @Valid SavingsTransactionCreateDto dto
    ) {
        User user = userService.getCurrentUserEntity();
        SavingsAccount account = savingsAccountService.getById(user, savingsAccountId);
        ObjectResponse responseBody = new ObjectResponse(
                "Savings transaction created successfully",
                HttpStatus.OK.toString(),
                savingsTransactionService.create(user, account, dto)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<ObjectResponse> getById(
            @RequestParam Long id
    ) {
        User user = userService.getCurrentUserEntity();
        ObjectResponse responseBody = new ObjectResponse(
                "Savings transaction with id <%s>".formatted(id),
                HttpStatus.OK.toString(),
                savingsTransactionService.getById(user, id)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ObjectListResponse> getAll(
            @RequestParam Long savingsAccountId,
            @RequestParam Integer pageSize,
            @RequestParam Integer pageNumber
    ) {
        User user = userService.getCurrentUserEntity();
        SavingsAccount account = savingsAccountService.getById(user, savingsAccountId);
        ObjectListResponse responseBody = new ObjectListResponse(
                "Savings transactions list: (PageNumber: %s, PageSize: %s)".formatted(pageNumber, pageSize),
                HttpStatus.OK.toString(),
                savingsTransactionService.getAll(user, account, pageSize, pageNumber)
                        .stream()
                        .map(transaction -> (Object) transaction)
                        .collect(Collectors.toList())
        );
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("PageSize", String.valueOf(pageSize));
        responseHeaders.add("PageNumber", String.valueOf(pageNumber));
        return ResponseEntity
                .ok()
                .headers(responseHeaders)
                .body(responseBody);
    }

    @PatchMapping("/update")
    public ResponseEntity<ObjectResponse> updateById(
            @RequestParam Long id,
            @RequestBody @Valid SavingsTransactionUpdateDto dto
    ) {
        User user = userService.getCurrentUserEntity();
        ObjectResponse responseBody = new ObjectResponse(
                "Savings transaction with id <%s> has been successfully edited".formatted(id),
                HttpStatus.OK.toString(),
                savingsTransactionService.update(user, id, dto)
        );
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ObjectResponse> deleteById(
            @RequestParam Long id
    ) {
        User user = userService.getCurrentUserEntity();
        SavingsTransaction deletedTransaction = savingsTransactionService.getById(user, id);
        savingsTransactionService.deleteById(user, id);
        ObjectResponse responseBody = new ObjectResponse(
                "Savings transaction with id <%s> has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                deletedTransaction
        );
        return ResponseEntity.ok(responseBody);
    }
}
