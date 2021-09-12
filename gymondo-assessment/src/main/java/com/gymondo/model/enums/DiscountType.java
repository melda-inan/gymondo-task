package com.gymondo.model.enums;

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
        switch (value) {
            case 0:
                return PERCENTAGE;
            case 1:
                return FIXED_AMOUNT;
            default:
                throw new IllegalArgumentException(value + " is not a supported discount type for vouchers.");
        }
    }
}
