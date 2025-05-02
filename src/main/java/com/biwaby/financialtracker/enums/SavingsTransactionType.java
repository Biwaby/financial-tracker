package com.biwaby.financialtracker.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SavingsTransactionType {
    INCOME("Income"),
    EXPENSE("Expense"),
    TRANSFER_INCOME("Transfer income"),
    TRANSFER_EXPENSE("Transfer expense"),
    CHANGING_CURRENCY("Changing currency");

    private final String displayName;

    SavingsTransactionType(String displayName) {
        this.displayName = displayName;
    }

    public static String getAllTypes() {
        StringBuilder sb = new StringBuilder();
        for (SavingsTransactionType type : SavingsTransactionType.values()) {
            sb.append(type.displayName).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static SavingsTransactionType getTypeByValue(String value) {
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("The <type> must not be empty. Available values: [%s]".formatted(getAllTypes()));
        }
        for (SavingsTransactionType st : SavingsTransactionType.values()) {
            if (st.getDisplayName().equalsIgnoreCase(value)) {
                return st;
            }
        }
        throw new IllegalArgumentException("Invalid <type> value: '%s'. Available values (case is not important): [%s]".formatted(value, getAllTypes()));
    }
}
