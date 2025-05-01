package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.create.WalletTransactionCreateDto;
import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.dto.update.WalletTransactionUpdateDto;
import com.biwaby.financialtracker.entity.Category;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.entity.Wallet;
import com.biwaby.financialtracker.entity.WalletTransaction;
import com.biwaby.financialtracker.service.CategoryService;
import com.biwaby.financialtracker.service.WalletService;
import com.biwaby.financialtracker.service.WalletTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/wallet-transactions")
@RequiredArgsConstructor
public class WalletTransactionController {

    private final WalletTransactionService walletTransactionService;
    private final WalletService walletService;
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> create(
            @RequestParam Long walletId,
            @RequestParam Long categoryId,
            @RequestBody @Valid WalletTransactionCreateDto dto
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Wallet wallet = walletService.getById(user, walletId);
        Category category = categoryService.getById(user, categoryId);
        ObjectResponse responseBody = new ObjectResponse(
                "Wallet transaction created successfully",
                HttpStatus.OK.toString(),
                walletTransactionService.create(user, wallet, category, dto)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<ObjectResponse> getById(
            @RequestParam Long id
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectResponse responseBody = new ObjectResponse(
                "Wallet transaction with id <%s>".formatted(id),
                HttpStatus.OK.toString(),
                walletTransactionService.getById(user, id)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ObjectListResponse> getAll(
            @RequestParam Long walletId,
            @RequestParam Integer pageSize,
            @RequestParam Integer pageNumber
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Wallet wallet = walletService.getById(user, walletId);
        ObjectListResponse responseBody = new ObjectListResponse(
                "Wallet transactions list: (PageNumber: %s, PageSize: %s)".formatted(pageNumber, pageSize),
                HttpStatus.OK.toString(),
                walletTransactionService.getAll(user, wallet, pageSize,  pageNumber)
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
            @RequestParam(required = false) Long categoryId,
            @RequestBody(required = false) @Valid WalletTransactionUpdateDto dto
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category category = categoryService.getById(user, categoryId);
        ObjectResponse responseBody = new ObjectResponse(
                "Wallet transaction with id <%s> has been successfully edited".formatted(id),
                HttpStatus.OK.toString(),
                walletTransactionService.update(user, id, category, dto)
        );
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ObjectResponse> deleteById(
            @RequestParam Long id
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WalletTransaction deletedTransaction = walletTransactionService.getById(user, id);
        walletTransactionService.deleteById(user, id);
        ObjectResponse responseBody = new ObjectResponse(
                "Wallet transaction with id <%s> has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                deletedTransaction
        );
        return ResponseEntity.ok(responseBody);
    }
}
