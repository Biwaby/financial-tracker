package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.entity.Limit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/limits")
@RequiredArgsConstructor
public class LimitController {

    @PostMapping("/add")
    public ResponseEntity<ObjectResponse> add(
            @RequestBody Limit limit
    ) {
        ObjectResponse response = new ObjectResponse(
                "Limit added successfully",
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
                "Limit with id <%s>".formatted(id),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ObjectListResponse> getAll() {
        ObjectListResponse response = new ObjectListResponse(
                "Limits list",
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<ObjectResponse> updateById(
            @RequestParam Long id,
            @RequestBody Limit limit
    ) {
        ObjectResponse response = new ObjectResponse(
                "Limit with id <%s> has been successfully edited".formatted(id),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ObjectResponse> deleteById(
            @RequestParam Long id
    ) {
        ObjectResponse response = new ObjectResponse(
                "Limit with id <%s> has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }
}
