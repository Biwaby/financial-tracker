package com.biwaby.financialtracker.controller.admin;

import com.biwaby.financialtracker.dto.response.DeleteResponse;
import com.biwaby.financialtracker.dto.response.EditResponse;
import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.entity.Currency;
import com.biwaby.financialtracker.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @PostMapping("/add")
    public ResponseEntity<ObjectResponse> add(
            @RequestBody Currency currency
    ) {
        ObjectResponse response = new ObjectResponse(
                "Currency added successfully",
                HttpStatus.OK.toString(),
                currencyService.add(currency)
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
                currencyService.getById(id)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ObjectListResponse> getAll() {
        ObjectListResponse response = new ObjectListResponse(
                "Currencies list",
                HttpStatus.OK.toString(),
                currencyService.getAll().stream()
                        .map(currency -> (Object) currency)
                        .collect(Collectors.toList())
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<EditResponse> editById(
            @RequestParam Long id,
            @RequestBody Currency currency
    ) {
        Currency toEdit = currencyService.getById(id);
        Currency oldCurrency = new Currency(
                toEdit.getId(),
                toEdit.getCode(),
                toEdit.getName()
        );
        Currency edited = currencyService.edit(id, currency);
        EditResponse response = new EditResponse(
                "Currency with id %s has been successfully edited".formatted(id),
                HttpStatus.OK.toString(),
                oldCurrency,
                edited
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteResponse> deleteById(
            @RequestParam Long id
    ) {
        Currency deleted = currencyService.getById(id);
        currencyService.delete(id);
        DeleteResponse response = new DeleteResponse(
                "Currency with id %s has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                deleted
        );
        return ResponseEntity.ok(response);
    }
}
