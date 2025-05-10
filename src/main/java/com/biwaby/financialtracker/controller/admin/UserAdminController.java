package com.biwaby.financialtracker.controller.admin;

import com.biwaby.financialtracker.dto.read.UserReadDto;
import com.biwaby.financialtracker.dto.response.ObjectListResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    // Users
    @GetMapping("/users/get-by-id")
    public ResponseEntity<ObjectResponse> getUserById(
            @RequestParam Long id
    ) {
        ObjectResponse responseBody = new ObjectResponse(
                "User with id <%s>".formatted(id),
                HttpStatus.OK.toString(),
                userService.getDtoById(id)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/users/get-by-username")
    public ResponseEntity<ObjectResponse> getUserByUsername(
            @RequestParam String username
    ) {
        ObjectResponse responseBody = new ObjectResponse(
                "User with username <%s>".formatted(username),
                HttpStatus.OK.toString(),
                userService.getDtoByUsername(username)
        );
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/users/get-all")
    public ResponseEntity<ObjectListResponse> getAllUsers(
            @RequestParam Integer pageSize,
            @RequestParam Integer pageNumber
    ) {
        ObjectListResponse responseBody = new ObjectListResponse(
                "Users list: (PageNumber: %s, PageSize: %s)".formatted(pageNumber, pageSize),
                HttpStatus.OK.toString(),
                userService.getAll(pageSize, pageNumber).stream()
                        .map(userDto -> (Object) userDto)
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

    @PostMapping("/users/update-role")
    public ResponseEntity<ObjectResponse> updateRole(
            @RequestParam Long userId,
            @RequestParam String authority
    ) {
        ObjectResponse responseBody = new ObjectResponse(
                "Role for user with id <%s> has been successfully updated".formatted(userId),
                HttpStatus.OK.toString(),
                userService.updateUserRole(userId, authority)
        );
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/users/delete-by-id")
    public ResponseEntity<ObjectResponse> deleteUserById(
            @RequestParam Long id
    ) {
        UserReadDto deletedUser = userService.getDtoById(id);
        userService.deleteById(id);
        ObjectResponse responseBody = new ObjectResponse(
                "User with id <%s> has been successfully deleted".formatted(id),
                HttpStatus.OK.toString(),
                deletedUser
        );
        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/users/delete-by-username")
    public ResponseEntity<ObjectResponse> deleteUserByUsername(
            @RequestParam String username
    ) {
        UserReadDto deletedUser = userService.getDtoByUsername(username);
        userService.deleteByUsername(username);
        ObjectResponse responseBody = new ObjectResponse(
                "User with username <%s> has been successfully deleted".formatted(username),
                HttpStatus.OK.toString(),
                deletedUser
        );
        return ResponseEntity.ok(responseBody);
    }
}
