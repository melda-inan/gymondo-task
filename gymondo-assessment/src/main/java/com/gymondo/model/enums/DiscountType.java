package com.gymondo.model.enums;

import java.util.Arrays;

public enum DiscountType {
    PERCENTAGE(0),
    FIXED_AMOUNT(1);

    private Integer value;


    DiscountType(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static DiscountType fromValue(Integer value) {
        return Arrays.stream(values())
                     .filter(it -> it.getValue().equals(value))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException(value + " is not a supported discount type for vouchers."));
    }
}
