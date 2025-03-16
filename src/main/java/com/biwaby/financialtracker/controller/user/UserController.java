package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.UserDto;
import com.biwaby.financialtracker.dto.UserEditDto;
import com.biwaby.financialtracker.dto.response.DeleteResponse;
import com.biwaby.financialtracker.dto.response.EditResponse;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.service.UserService;
import jakarta.validation.Valid;
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
                "User with username %s".formatted(userDto.getUsername()),
                HttpStatus.OK.toString(),
                userDto
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/edit-self")
    public ResponseEntity<EditResponse> editSelf(
            @RequestBody @Valid UserEditDto userEditDto
    ) {
        UserDto editUser = userService.getSelf();
        UserDto beforeEditUser = new UserDto(
                editUser.getUsername(),
                editUser.getRoleName(),
                editUser.getRegisteredAt()
        );
        String responseText = "User with username %s has been successfully edited".formatted(editUser.getUsername());
        UserDto afterEditUser = userService.editSelf(userEditDto);
        EditResponse response = new EditResponse(
                responseText,
                HttpStatus.OK.toString(),
                beforeEditUser,
                afterEditUser
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-self")
    public ResponseEntity<DeleteResponse> deleteSelf() {
        UserDto userDto = userService.getSelf();
        userService.deleteSelf();
        DeleteResponse response = new DeleteResponse(
                "User with username %s has been successfully deleted".formatted(userDto.getUsername()),
                HttpStatus.OK.toString(),
                null
        );
        return ResponseEntity.ok(response);
    }
}
