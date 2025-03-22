package com.biwaby.financialtracker.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyUpdateDto {

    @Size(min = 3, max = 3, message = "The currency code must contain 3 characters")
    String code;

    @Size(min = 3, max = 100, message = "The currency name must contain from 3 to 100 characters")
    String name;
}
