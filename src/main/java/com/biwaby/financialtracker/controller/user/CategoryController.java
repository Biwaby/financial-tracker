package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.update.CategoryUpdateDto;
import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.entity.Category;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectResponse responseBody = new ObjectResponse(
                "Category added successfully",
                HttpStatus.OK.toString(),
                categoryService.create(user, category)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<ObjectResponse> getById(
            @RequestParam Long id
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectResponse responseBody = new ObjectResponse(
                "Category with id <%s>".formatted(id),
                HttpStatus.OK.toString(),
                categoryService.getById(user, id)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ObjectListResponse> getAll(
            @RequestParam Integer pageSize,
            @RequestParam Integer pageNumber
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectListResponse responseBody = new ObjectListResponse(
                "Categories list: (PageNumber: %s, PageSize: %s)".formatted(pageNumber, pageSize),
                HttpStatus.OK.toString(),
                categoryService.getAll(user, pageSize, pageNumber).stream()
                        .map(category -> (Object) category)
                        .collect(Collectors.toList())
        );
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("PageSize", String.valueOf(pageSize));
        responseHeaders.add("PageNumber", String.valueOf(pageNumber));
        return ResponseEntity
                .ok()
                .headers(responseHeaders)
                .body(responseBody);
    }

    @PatchMapping("/update")
    public ResponseEntity<ObjectResponse> updateById(
            @RequestParam Long id,
            @RequestBody @Valid CategoryUpdateDto dto
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectResponse responseBody = new ObjectResponse(
                "Category with id <%s> has been successfully edited".formatted(id),
                HttpStatus.OK.toString(),
                categoryService.update(user, id, dto)
        );
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ObjectResponse> deleteById(
            @RequestParam Long id
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category deletedCategory = categoryService.getById(user, id);
        categoryService.deleteById(user, id);
        ObjectResponse responseBody = new ObjectResponse(
                "Category with id <%s> has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                deletedCategory
        );
        return ResponseEntity.ok(responseBody);
    }
}
