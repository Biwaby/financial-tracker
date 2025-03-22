package com.biwaby.financialtracker.controller.admin;

import com.biwaby.financialtracker.dto.response.DeleteResponse;
import com.biwaby.financialtracker.dto.response.EditResponse;
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
        ObjectResponse response = new ObjectResponse(
                "Role added successfully",
                HttpStatus.OK.toString(),
                roleService.create(role)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<ObjectResponse> getById(
            @RequestParam Long id
    ) {
        ObjectResponse response = new ObjectResponse(
                "Role with id <%s>".formatted(id),
                HttpStatus.OK.toString(),
                roleService.getById(id)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ObjectListResponse> getAll() {
        ObjectListResponse response = new ObjectListResponse(
                "Roles list",
                HttpStatus.OK.toString(),
                roleService.getAll().stream()
                        .map(role -> (Object) role)
                        .collect(Collectors.toList())
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<EditResponse> editById(
            @RequestParam Long id,
            @Valid @RequestBody Role role
    ) {
        Role toEdit = roleService.getById(id);
        Role oldRole = new Role(
                toEdit.getId(),
                toEdit.getName()
        );
        Role edited = roleService.update(id, role);
        EditResponse response = new EditResponse(
                "Role with id <%s> has been successfully edited".formatted(id),
                HttpStatus.OK.toString(),
                oldRole,
                edited
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteResponse> deleteById(
            @RequestParam Long id
    ) {
        Role deleted = roleService.getById(id);
        roleService.deleteById(id);
        DeleteResponse response = new DeleteResponse(
                "Role with id <%s> has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                deleted
        );
        return ResponseEntity.ok(response);
    }
}
