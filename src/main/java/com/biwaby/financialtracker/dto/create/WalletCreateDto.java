package com.biwaby.financialtracker.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletCreateDto {

    @Size(min = 3, max = 255, message = "The <name> must be between 3 and 255 characters long.")
    @NotBlank(message = "The <name> must not be empty.")
    private String name;

    @NotBlank(message = "The <currencyLetterCode> must not be empty.")
    @Pattern(regexp = "^[a-zA-Z]{3}$", message = "The <currencyLetterCode> must consist of 3 case-insensitive characters of the Latin alphabet.")
    private String currencyLetterCode;
}
