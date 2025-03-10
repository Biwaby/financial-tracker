package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.response.DeleteResponse;
import com.biwaby.financialtracker.dto.response.EditResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<ObjectResponse> register(
            @RequestBody User user
    ) {
        ObjectResponse response = new ObjectResponse(
                "User with username %s registered successfully".formatted(user.getUsername()),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ObjectResponse> login(
            @RequestBody User user
    ) {
        ObjectResponse response = new ObjectResponse(
                "Welcome back, %s!".formatted(user.getUsername()),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-self")
    public ResponseEntity<ObjectResponse> getSelf() {
        User user = new User();
        ObjectResponse response = new ObjectResponse(
                "User with username %s".formatted(user.getUsername()),
                HttpStatus.OK.toString(),
                user
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit-self")
    public ResponseEntity<EditResponse> editSelf(
            @RequestBody User user
    ) {
        User beforeUser = new User();
        EditResponse response = new EditResponse(
                "User with username %s has been successfully edited".formatted(beforeUser.getUsername()),
                HttpStatus.OK.toString(),
                beforeUser,
                user
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-self")
    public ResponseEntity<DeleteResponse> deleteSelf() {
        User user = new User();
        DeleteResponse response = new DeleteResponse(
                "User with username %s has been successfully deleted".formatted(user.getUsername()),
                HttpStatus.OK.toString(),
                user
        );
        return ResponseEntity.ok(response);
    }
}
