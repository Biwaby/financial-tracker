package com.biwaby.financialtracker.dto.update;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingsAccountUpdateDto {

    @Size(min = 3, max = 255, message = "The <name> must be between 3 and 255 characters long.")
    private String name;

    private BigDecimal targetAmount;

    private BigDecimal currentAmount;

    @Pattern(
            regexp = "^(?:19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$",
            message = "The <deadlineDate> must be in the format 'yyyy-MM-dd'."
    )
    private String deadlineDate;

    @Size(min = 3, max = 255, message = "The <status> must be between 3 and 255 characters long.")
    private String status;
}
