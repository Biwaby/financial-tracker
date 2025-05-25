package com.biwaby.financialtracker.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SavingsAccountStatus {
    ACTIVE("Active"),
    COMPLETED("Completed");

    private final String displayName;

    SavingsAccountStatus(String displayName) {
        this.displayName = displayName;
    }

    public static String getAllStatuses() {
        StringBuilder sb = new StringBuilder();
        for (SavingsAccountStatus status : SavingsAccountStatus.values()) {
            sb.append(status.displayName).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static SavingsAccountStatus getStatusByValue(String value) {
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("The <status> must not be empty. Available values: [%s]".formatted(getAllStatuses()));
        }
        for (SavingsAccountStatus status : SavingsAccountStatus.values()) {
            if (status.getDisplayName().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid <status> value: '%s'. Available values (case is not important): [%s]".formatted(value, getAllStatuses()));
    }
}
