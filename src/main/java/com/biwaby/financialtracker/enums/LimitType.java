package com.biwaby.financialtracker.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public enum LimitType {
    MONTHLY("Monthly", 1, ChronoUnit.MONTHS),
    WEEKLY("Weekly", 1, ChronoUnit.WEEKS),
    PERMANENT("Permanent", 1, ChronoUnit.YEARS);

    private final String displayName;
    private final Integer amount;
    private final TemporalUnit temporalUnit;

    LimitType(String displayName, Integer amount, TemporalUnit temporalUnit) {
        this.displayName = displayName;
        this.amount = amount;
        this.temporalUnit = temporalUnit;
    }

    public static String getAllTypes() {
        StringBuilder sb = new StringBuilder();
        for (LimitType type : LimitType.values()) {
            sb.append(type.getDisplayName()).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    public LocalDateTime addTo(LocalDateTime localDateTime, Integer multiplier) {
        return localDateTime.plus((long) amount * multiplier, temporalUnit);
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static LimitType getTypeByValue(String value) {
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("The <type> must not be empty. Available values: [%s]".formatted(getAllTypes()));
        }
        for (LimitType lt : LimitType.values()) {
            if (lt.getDisplayName().equalsIgnoreCase(value)) {
                return lt;
            }
        }
        throw new IllegalArgumentException("Invalid <type> value: '%s'. Available values (case is not important): [%s]".formatted(value, getAllTypes()));
    }
}
