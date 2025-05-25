package com.biwaby.financialtracker.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsAccountCreateDto {

    @Size(min = 3, max = 255, message = "The <name> must be between 3 and 255 characters long.")
    @NotBlank(message = "The <name> must not be empty.")
    private String name;

    @NotBlank(message = "The <currencyLetterCode> must not be empty.")
    @Pattern(regexp = "^[a-zA-Z]{3}$", message = "The <currencyLetterCode> must consist of 3 case-insensitive characters of the Latin alphabet.")
    private String currencyLetterCode;

    @NotNull(message = "The <targetAmount> must not be empty.")
    private BigDecimal targetAmount;

    @Pattern(
            regexp = "^(?:19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$",
            message = "The <deadlineDate> must be in the format 'yyyy-MM-dd'."
    )
    private String deadlineDate;
}
