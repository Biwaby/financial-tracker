package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.UserDto;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.service.UserService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get-self")
    public ResponseEntity<ObjectResponse> getSelf() {
        UserDto userDto = userService.getSelf();
        ObjectResponse response = new ObjectResponse(
                "User with username <%s>".formatted(userDto.getUsername()),
                HttpStatus.OK.toString(),
                userDto
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-username")
    public ResponseEntity<ObjectResponse> updateUsername(
            @RequestParam
            @Pattern(
                    regexp = "^[A-Za-z]{5,20}$",
                    message = "The username must contain only uppercase or lowercase Latin letters and must be between 5 and 20 characters long."
            )
            String username
    ) {
        ObjectResponse response = new ObjectResponse(
                "The username has been updated. You need to log in again.",
                HttpStatus.OK.toString(),
                userService.updateUsername(username)
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-password")
    public ResponseEntity<ObjectResponse> updatePassword(
            @RequestParam
            @Pattern(
                    regexp = "^(?=(?:[^a-z]*[a-z]){3})(?=(?:[^A-Z]*[A-Z]){3})(?=(?:[^0-9]*[0-9]){2})[A-Za-z0-9]{8,40}$",
                    message = "The password must contain at least 3 lowercase Latin letters, at least 3 uppercase Latin letters, at least 2 digits from 0 to 9 inclusive and must be between 8 and 40 characters long."
            )
            String password
    ) {
        ObjectResponse response = new ObjectResponse(
                "The password has been updated. You need to log in again.",
                HttpStatus.OK.toString(),
                userService.updatePassword(password)
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-self")
    public ResponseEntity<ObjectResponse> deleteSelf() {
        UserDto userDto = userService.getSelf();
        userService.deleteSelf();
        ObjectResponse response = new ObjectResponse(
                "User with username <%s> has been successfully deleted".formatted(userDto.getUsername()),
                HttpStatus.OK.toString(),
                userDto
        );
        return ResponseEntity.ok(response);
    }
}
