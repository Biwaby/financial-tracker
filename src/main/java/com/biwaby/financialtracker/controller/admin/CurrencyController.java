package com.biwaby.financialtracker.controller.admin;

import com.biwaby.financialtracker.dto.update.CurrencyUpdateDto;
import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.entity.Currency;
import com.biwaby.financialtracker.service.CurrencyService;
import jakarta.validation.Valid;
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

    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> create(
            @RequestBody @Valid Currency currency
    ) {
        ObjectResponse response = new ObjectResponse(
                "Currency added successfully",
                HttpStatus.OK.toString(),
                currencyService.create(currency)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<ObjectResponse> getById(
            @RequestParam Long id
    ) {
        ObjectResponse response = new ObjectResponse(
                "Currency with id <%s>".formatted(id),
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

    @PatchMapping("/update")
    public ResponseEntity<ObjectResponse> updateById(
            @RequestParam Long id,
            @RequestBody @Valid CurrencyUpdateDto dto
    ) {
        ObjectResponse response = new ObjectResponse(
                "Currency with id <%s> has been successfully edited".formatted(id),
                HttpStatus.OK.toString(),
                currencyService.update(id, dto)
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ObjectResponse> deleteById(
            @RequestParam Long id
    ) {
        Currency deletedCurrency = currencyService.getById(id);
        currencyService.deleteById(id);
        ObjectResponse response = new ObjectResponse(
                "Currency with id <%s> has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                deletedCurrency
        );
        return ResponseEntity.ok(response);
    }
}
