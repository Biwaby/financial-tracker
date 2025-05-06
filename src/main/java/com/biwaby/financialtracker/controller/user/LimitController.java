package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.create.LimitCreateDto;
import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.dto.update.LimitUpdateDto;
import com.biwaby.financialtracker.entity.Limit;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.entity.Wallet;
import com.biwaby.financialtracker.service.LimitService;
import com.biwaby.financialtracker.service.UserService;
import com.biwaby.financialtracker.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/limits")
@RequiredArgsConstructor
public class LimitController {

    private final LimitService limitService;
    private final WalletService walletService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> create(
            @RequestParam Long walletId,
            @RequestBody @Valid LimitCreateDto dto
    ) {
        User user = userService.getCurrentUserEntity();
        Wallet wallet = walletService.getById(user, walletId);
        ObjectResponse responseBody = new ObjectResponse(
                "Limit created successfully",
                HttpStatus.OK.toString(),
                limitService.create(user, wallet, dto)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<ObjectResponse> getById(
            @RequestParam Long id
    ) {
        User user = userService.getCurrentUserEntity();
        ObjectResponse responseBody = new ObjectResponse(
                "Limit with id <%s>".formatted(id),
                HttpStatus.OK.toString(),
                limitService.getById(user, id)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-by-wallet-id")
    public ResponseEntity<ObjectResponse> getByWalletId(
            @RequestParam Long walletId
    ) {
        User user = userService.getCurrentUserEntity();
        Wallet wallet = walletService.getById(user, walletId);
        ObjectResponse responseBody = new ObjectResponse(
                "Limit for wallet with walletId <%s>".formatted(walletId),
                HttpStatus.OK.toString(),
                limitService.getByWallet(user, wallet)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ObjectListResponse> getAll() {
        User user = userService.getCurrentUserEntity();
        ObjectListResponse responseBody = new ObjectListResponse(
                "Limits list",
                HttpStatus.OK.toString(),
                limitService.getAll(user)
                        .stream()
                        .map(limit -> (Object) limit)
                        .collect(Collectors.toList())
        );
        return ResponseEntity.ok(responseBody);
    }

    @PatchMapping("/update")
    public ResponseEntity<ObjectResponse> updateById(
            @RequestParam Long id,
            @RequestBody @Valid LimitUpdateDto dto
    ) {
        User user = userService.getCurrentUserEntity();
        ObjectResponse responseBody = new ObjectResponse(
                "Limit with id <%s> has been successfully edited".formatted(id),
                HttpStatus.OK.toString(),
                limitService.update(user, id, dto)
        );
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ObjectResponse> deleteById(
            @RequestParam Long id
    ) {
        User user = userService.getCurrentUserEntity();
        Limit deletedLimit = limitService.getById(user, id);
        limitService.deleteById(user, id);
        ObjectResponse responseBody = new ObjectResponse(
                "Limit with id <%s> has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                deletedLimit
        );
        return ResponseEntity.ok(responseBody);
    }
}
