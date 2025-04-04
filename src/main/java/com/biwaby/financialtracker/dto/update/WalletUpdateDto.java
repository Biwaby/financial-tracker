package com.biwaby.financialtracker.dto.update;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletUpdateDto {

    @Size(min = 3, max = 255, message = "The <name> must be between 3 and 255 characters long.")
    private String name;

    private BigDecimal balance;
}
