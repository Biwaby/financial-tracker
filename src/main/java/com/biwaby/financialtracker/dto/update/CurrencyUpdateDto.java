package com.biwaby.financialtracker.dto.update;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyUpdateDto {

    @Pattern(regexp = "^[0-9]{3}$", message = "The <code> must contain 3 digits from 0 to 9.")
    private String code;

    @Pattern(regexp = "^[a-zA-Z]{3}$", message = "The <letterCode> must consist of 3 case-insensitive characters of the Latin alphabet.")
    private String letterCode;

    @Size(min = 3, max = 100, message = "The <name> must contain from 3 to 100 characters")
    private String name;
}
