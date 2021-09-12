package com.gymondo.model.enums;

import java.util.Arrays;

public enum CustomerSubscriptionStatus {
    ACTIVE("A"),
    PAUSED("P"),
    CANCELED("C");

    private String value;

    CustomerSubscriptionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CustomerSubscriptionStatus fromValue(String value) {
        return Arrays.stream(values())
                     .filter(it -> it.getValue().equals(value))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException(String.format("%s is not a supported status for subscriptions.", value)));
    }
}

