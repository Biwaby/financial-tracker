package com.biwaby.financialtracker.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateDto {

    @Pattern(
            regexp = "^[A-Za-z]{5,20}$",
            message = "The username must contain only uppercase or lowercase Latin letters and must be between 5 and 20 characters long."
    )
    private String username;

    @Pattern(
            regexp = "^(?=(?:[^a-z]*[a-z]){3})(?=(?:[^A-Z]*[A-Z]){3})(?=(?:[^0-9]*[0-9]){2})[A-Za-z0-9]{8,40}$",
            message = "The password must contain at least 3 lowercase Latin letters, at least 3 uppercase Latin letters, at least 2 digits from 0 to 9 inclusive and must be between 8 and 40 characters long."
    )
    private String password;
}
