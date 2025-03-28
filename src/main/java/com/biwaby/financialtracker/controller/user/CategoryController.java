package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.update.CategoryUpdateDto;
import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.entity.Category;
import com.biwaby.financialtracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> create(
            @RequestBody @Valid Category category
    ) {
        ObjectResponse response = new ObjectResponse(
                "Category added successfully",
                HttpStatus.OK.toString(),
                categoryService.create(category)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<ObjectResponse> getById(
            @RequestParam Long id
    ) {
        ObjectResponse response = new ObjectResponse(
                "Category with id <%s>".formatted(id),
                HttpStatus.OK.toString(),
                categoryService.getById(id)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ObjectListResponse> getAll(
            @RequestParam Integer pageSize,
            @RequestParam Integer pageNumber
    ) {
        ObjectListResponse response = new ObjectListResponse(
                "Categories list: (PageNumber: %s, PageSize: %s)".formatted(pageNumber, pageSize),
                HttpStatus.OK.toString(),
                categoryService.getAll(pageSize, pageNumber).stream()
                        .map(category -> (Object) category)
                        .collect(Collectors.toList())
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update")
    public ResponseEntity<ObjectResponse> updateById(
            @RequestParam Long id,
            @RequestBody @Valid CategoryUpdateDto dto
    ) {
        ObjectResponse response = new ObjectResponse(
                "Category with id <%s> has been successfully edited".formatted(id),
                HttpStatus.OK.toString(),
                categoryService.update(id, dto)
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ObjectResponse> deleteById(
            @RequestParam Long id
    ) {
        Category deletedCategory = categoryService.getById(id);
        categoryService.deleteById(id);
        ObjectResponse response = new ObjectResponse(
                "Category with id <%s> has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                deletedCategory
        );
        return ResponseEntity.ok(response);
    }
}
