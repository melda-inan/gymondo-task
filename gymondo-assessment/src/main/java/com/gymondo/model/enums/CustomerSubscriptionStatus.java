package com.gymondo.model.enums;

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
        switch (value) {
            case "A":
                return CustomerSubscriptionStatus.ACTIVE;
            case "P":
                return CustomerSubscriptionStatus.PAUSED;
            case "C":
                return CustomerSubscriptionStatus.CANCELED;
            default:
                throw new IllegalArgumentException(value + " is not a supported status for subscriptions.");
        }
    }
}

