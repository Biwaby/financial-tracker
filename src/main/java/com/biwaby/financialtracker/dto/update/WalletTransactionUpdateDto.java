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
public class WalletTransactionUpdateDto {

    @Size(min = 3, max = 255, message = "The <name> must be between 3 and 255 characters long.")
    private String name;

    @Size(min = 3, max = 255, message = "The <type> must be between 3 and 255 characters long.")
    private String type;

    private BigDecimal amount;

    @Pattern(
            regexp = "^(?:19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])T(?:(0[0-9]|1[0-9]|2[0-3]):(?:(0[0-9]|[1-5][0-9]):(?:(0[0-9]|[1-5][0-9])(?:\\.\\d{1,3})?)?)?)?$",
            message = "The <transactionDate> must be in the format 'yyyy-MM-ddTHH:mm:ss' or 'yyyy-MM-ddTHH:mm:ss.SSS'."
    )
    private String transactionDate;

    private String description;
}
