package com.biwaby.financialtracker.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequest {

    @NotEmpty(message = "The username must not be empty.")
    @NotBlank(message = "The username must not be empty.")
    @Pattern(
            regexp = "^[A-Za-z]{5,20}$",
            message = "The username must contain only uppercase or lowercase Latin letters and must be between 5 and 20 characters long."
    )
    private String username;

    @NotBlank(message = "The password must not be empty.")
    @NotEmpty(message = "The password must not be empty.")
    @Pattern(
            regexp = "^(?=(?:[^a-z]*[a-z]){3})(?=(?:[^A-Z]*[A-Z]){3})(?=(?:[^0-9]*[0-9]){2})[A-Za-z0-9]{8,40}$",
            message = "The password must contain at least 3 lowercase Latin letters, at least 3 uppercase Latin letters, at least 2 digits from 0 to 9 inclusive and must be between 8 and 40 characters long."
    )
    private String password;
}
