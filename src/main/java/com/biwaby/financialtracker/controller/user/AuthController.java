package com.biwaby.financialtracker.controller.user;

import com.biwaby.financialtracker.dto.auth.AuthRequest;
import com.biwaby.financialtracker.dto.response.ObjectResponse;
import com.biwaby.financialtracker.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public ResponseEntity<ObjectResponse> signUp(
            @RequestBody @Valid AuthRequest request
    ) {
        ObjectResponse response = new ObjectResponse(
                "User with username %s registered successfully".formatted(request.getUsername()),
                HttpStatus.OK.toString(),
                authenticationService.signUp(request)
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ObjectResponse> signIn(
            @RequestBody @Valid AuthRequest request
    ) {
        ObjectResponse response = new ObjectResponse(
                "Welcome back, %s!".formatted(request.getUsername()),
                HttpStatus.OK.toString(),
                authenticationService.signIn(request)
        );
        return ResponseEntity.ok(response);
    }
}
