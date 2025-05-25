package com.biwaby.financialtracker.controller.admin;

import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.entity.Role;
import com.biwaby.financialtracker.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<ObjectResponse> create(
            @Valid @RequestBody Role role
    ) {
        ObjectResponse responseBody = new ObjectResponse(
                "Role added successfully",
                HttpStatus.OK.toString(),
                roleService.create(role)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<ObjectResponse> getById(
            @RequestParam Long id
    ) {
        ObjectResponse responseBody = new ObjectResponse(
                "Role with id <%s>".formatted(id),
                HttpStatus.OK.toString(),
                roleService.getById(id)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ObjectListResponse> getAll() {
        ObjectListResponse responseBody = new ObjectListResponse(
                "Roles list",
                HttpStatus.OK.toString(),
                roleService.getAll().stream()
                        .map(role -> (Object) role)
                        .collect(Collectors.toList())
        );
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/update")
    public ResponseEntity<ObjectResponse> updateById(
            @RequestParam Long id,
            @Valid @RequestBody Role role
    ) {
        ObjectResponse responseBody = new ObjectResponse(
                "Role with id <%s> has been successfully edited".formatted(id),
                HttpStatus.OK.toString(),
                roleService.update(id, role)
        );
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ObjectResponse> deleteById(
            @RequestParam Long id
    ) {
        Role deletedRole = roleService.getById(id);
        roleService.deleteById(id);
        ObjectResponse responseBody = new ObjectResponse(
                "Role with id <%s> has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                deletedRole
        );
        return ResponseEntity.ok(responseBody);
    }
}
