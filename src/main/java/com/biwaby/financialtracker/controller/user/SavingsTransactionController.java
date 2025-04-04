package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.entity.SavingsTransaction;
import org.springframework.http.HttpHeaders;
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
        ObjectResponse responseBody = new ObjectResponse(
                "Savings transaction created successfully",
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
                "Savings transaction with id <%s>".formatted(id),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ObjectListResponse> getAll(
            @RequestParam Integer pageSize,
            @RequestParam Integer pageNumber
    ) {
        ObjectListResponse responseBody = new ObjectListResponse(
                "Savings transactions list: (PageNumber: %s, PageSize: %s)".formatted(pageNumber, pageSize),
                HttpStatus.OK.toString(),
                null
        );
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("PageSize", String.valueOf(pageSize));
        responseHeaders.add("PageNumber", String.valueOf(pageNumber));
        return ResponseEntity
                .ok()
                .headers(responseHeaders)
                .body(responseBody);
    }

    @PutMapping("/update")
    public ResponseEntity<ObjectResponse> updateById(
            @RequestParam Long id,
            @RequestBody SavingsTransaction savingsTransaction
    ) {
        ObjectResponse responseBody = new ObjectResponse(
                "Savings transaction with id <%s> has been successfully edited".formatted(id),
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
                "Savings transaction with id <%s> has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(responseBody);
    }
}
