package com.biwaby.financialtracker.context;

import com.biwaby.financialtracker.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletOperationContext {

    private BigDecimal oldBalance;
    private Category depositCategory;
    private BigDecimal depositAmount;
    private Category withdrawCategory;
    private BigDecimal withdrawAmount;
    private BigDecimal transferAmount;
}
