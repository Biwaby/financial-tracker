package com.biwaby.financialtracker.dto.create;

import jakarta.validation.constraints.NotBlank;
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

    @Size(min = 3, max = 3, message = "The <currencyCode> must contain 3 characters.")
    @NotBlank(message = "The <currencyCode> must not be empty.")
    private String currencyCode;
}
