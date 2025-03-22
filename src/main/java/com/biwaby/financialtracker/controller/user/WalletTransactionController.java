package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.response.DeleteResponse;
import com.biwaby.financialtracker.dto.response.EditResponse;
import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.entity.WalletTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallet-transactions")
@RequiredArgsConstructor
public class WalletTransactionController {

    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> create(
            @RequestBody WalletTransaction walletTransaction
    ) {
        ObjectResponse response = new ObjectResponse(
                "Wallet transaction created successfully",
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<ObjectResponse> getById(
            @RequestParam Long id
    ) {
        ObjectResponse response = new ObjectResponse(
                "Wallet transaction with id <%s>".formatted(id),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ObjectListResponse> getAll(
            @RequestParam Integer pageSize,
            @RequestParam Integer pageNumber
    ) {
        ObjectListResponse response = new ObjectListResponse(
                "Wallet transactions list: (PageNumber: %s, PageSize: %s)".formatted(pageNumber, pageSize),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<EditResponse> edit(
            @RequestParam Long id,
            @RequestBody WalletTransaction walletTransaction
    ) {
        EditResponse response = new EditResponse(
                "Wallet transaction with id <%s> has been successfully edited".formatted(id),
                HttpStatus.OK.toString(),
                null,
                null
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteResponse> deleteById(
            @RequestParam Long id
    ) {
        DeleteResponse response = new DeleteResponse(
                "Wallet transaction with id <%s> has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }
}
