package com.biwaby.financialtracker.controller.admin;

import com.biwaby.financialtracker.dto.response.DeleteResponse;
import com.biwaby.financialtracker.dto.response.EditResponse;
import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.entity.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    @PostMapping("/add")
    public ResponseEntity<ObjectResponse> add(
            @RequestBody Currency currency
    ) {
        ObjectResponse response = new ObjectResponse(
                "Currency added successfully",
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
                "Currency with id %s".formatted(id),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ObjectListResponse> getAll() {
        ObjectListResponse response = new ObjectListResponse(
                "Currencies list",
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<EditResponse> editById(
            @RequestParam Long id,
            @RequestBody Currency currency
    ) {
        EditResponse response = new EditResponse(
                "Currency with id %s has been successfully edited".formatted(id),
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
                "Currency with id %s has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }
}
