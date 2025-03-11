package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.response.DeleteResponse;
import com.biwaby.financialtracker.dto.response.EditResponse;
import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.entity.SavingsTransaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/savings-transactions")
public class SavingsTransactionController {

    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> create(
            @RequestBody SavingsTransaction savingsTransaction
    ) {
        ObjectResponse response = new ObjectResponse(
                "Savings transaction created successfully",
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
                "Savings transaction with id %s".formatted(id),
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
                "Savings transactions list: (PageNumber: %s, PageSize: %s)".formatted(pageNumber, pageSize),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<EditResponse> edit(
            @RequestParam Long id,
            @RequestBody SavingsTransaction savingsTransaction
    ) {
        EditResponse response = new EditResponse(
                "Savings transaction with id %s has been successfully edited".formatted(id),
                HttpStatus.OK.toString(),
                null,
                null
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteResponse> delete(
            @RequestParam Long id
    ) {
        DeleteResponse response = new DeleteResponse(
                "Savings transaction with id %s has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }
}
