package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.entity.SavingsAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/savings-accounts")
public class SavingsAccountController {

    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> create(
            @RequestBody SavingsAccount savingsAccount
    ) {
        ObjectResponse responseBody = new ObjectResponse(
                "Savings account created successfully",
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<ObjectResponse> getById(
            @RequestParam Long id
    ) {
        ObjectResponse responseBody = new ObjectResponse(
                "Savings account with id <%s>".formatted(id),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ObjectListResponse> getAll() {
        ObjectListResponse responseBody = new ObjectListResponse(
                "Savings accounts list",
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/update")
    public ResponseEntity<ObjectResponse> updateById(
            @RequestParam Long id,
            @RequestBody SavingsAccount savingsAccount
    ) {
        ObjectResponse responseBody = new ObjectResponse(
                "Savings account with id <%s> has been successfully edited".formatted(id),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ObjectResponse> deleteById(
            @RequestParam Long id
    ) {
        ObjectResponse responseBody = new ObjectResponse(
                "Savings account with id <%s> has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(responseBody);
    }
}
