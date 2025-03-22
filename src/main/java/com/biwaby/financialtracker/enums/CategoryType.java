package com.biwaby.financialtracker.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CategoryType {
    INCOME("Income"),
    EXPENSE("Expense"),
    OTHER("Other"),
    BOTH("Both");

    public static String getAllTypes() {
        StringBuilder sb = new StringBuilder();
        for (CategoryType type : CategoryType.values()) {
            sb.append(type.getDisplayName()).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    private final String displayName;

    CategoryType(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static CategoryType getCategoryType(String type) {
        if (type.isEmpty()) {
            throw new IllegalArgumentException("The <type> must not be empty. Available values: [%s]".formatted(getAllTypes()));
        }
        for (CategoryType ct : CategoryType.values()) {
            if (ct.getDisplayName().equalsIgnoreCase(type)) {
                return ct;
            }
        }
        throw new IllegalArgumentException("Invalid <type> value: '%s'. Available values (case is not important): [%s]".formatted(type, getAllTypes()));
    }
}