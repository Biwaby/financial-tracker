package com.biwaby.financialtracker.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LimitCreateDto {

    @NotBlank(message = "The <type> must not be empty.")
    @Size(min = 3, max = 255, message = "The <type> must be between 3 and 255 characters long.")
    private String type;

    @NotNull(message = "The <targetAmount> must not be empty.")
    private BigDecimal targetAmount;
}
