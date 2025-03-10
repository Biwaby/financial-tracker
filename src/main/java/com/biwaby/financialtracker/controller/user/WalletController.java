package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.response.DeleteResponse;
import com.biwaby.financialtracker.dto.response.EditResponse;
import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.entity.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> createWallet(
            @RequestBody Wallet wallet
    ) {
        ObjectResponse response = new ObjectResponse(
                "Wallet created successfully",
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
                "Wallet with id %s".formatted(id),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ObjectListResponse> getAll() {
        ObjectListResponse response = new ObjectListResponse(
                "Wallets list",
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<EditResponse> edit(
            @RequestParam Long id,
            @RequestBody Wallet wallet
    ) {
        EditResponse response = new EditResponse(
                "Wallet with id %s has been successfully edited".formatted(id),
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
                "Wallet with id %s has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }
}
