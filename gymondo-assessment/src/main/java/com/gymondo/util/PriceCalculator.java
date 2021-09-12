package com.gymondo.util;

public final class PriceCalculator {

    public static Double calculateDiscountPrice(Double price, Integer discountPercentage) {
        return price * (100 - discountPercentage) / 100;
    }

    public static Double calculateDiscountPrice(Double price, Double discountAmount) {
        return price - discountAmount;
    }

    public static Double calculateTax(Double price, Integer taxPercentage) {
        return price * taxPercentage / 100;
    }
}
