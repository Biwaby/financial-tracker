package com.biwaby.financialtracker.controller.admin;

import com.biwaby.financialtracker.dto.UserDto;
import com.biwaby.financialtracker.dto.response.DeleteResponse;
import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    // Users
    @GetMapping("/users/get-by-id")
    public ResponseEntity<ObjectResponse> getUserById(
            @RequestParam Long id
    ) {
        ObjectResponse response = new ObjectResponse(
                "User with id %s".formatted(id),
                HttpStatus.OK.toString(),
                userService.getById(id)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/get-by-username")
    public ResponseEntity<ObjectResponse> getUserByUsername(
            @RequestParam String username
    ) {
        ObjectResponse response = new ObjectResponse(
                "User with username %s".formatted(username),
                HttpStatus.OK.toString(),
                userService.getByUsername(username)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/get-all")
    public ResponseEntity<ObjectListResponse> getAllUsers(
            @RequestParam Integer pageSize,
            @RequestParam Integer pageNumber
    ) {
        ObjectListResponse response = new ObjectListResponse(
                "Users list: (PageNumber: %s, PageSize: %s)".formatted(pageNumber, pageSize),
                HttpStatus.OK.toString(),
                userService.getAll(pageSize, pageNumber).stream()
                        .map(userDto -> (Object) userDto)
                        .collect(Collectors.toList())
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/delete-by-id")
    public ResponseEntity<DeleteResponse> deleteUserById(
            @RequestParam Long id
    ) {
        UserDto deleted = userService.getById(id);
        userService.deleteById(id);
        DeleteResponse response = new DeleteResponse(
                "User with id %s has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                deleted
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/delete-by-username")
    public ResponseEntity<DeleteResponse> deleteUserByUsername(
            @RequestParam String username
    ) {
        UserDto deleted = userService.getByUsername(username);
        userService.deleteByUsername(username);
        DeleteResponse response = new DeleteResponse(
                "User with username %s has been successfully deleted".formatted(username),
                HttpStatus.OK.toString(),
                deleted
        );
        return ResponseEntity.ok(response);
    }
}
