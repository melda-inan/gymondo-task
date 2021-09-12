package com.gymondo.util;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public final class DateUtil {

    public static long getDayDifference(LocalDate startDate, LocalDate endDate) {
        return DAYS.between(startDate, endDate);
    }
}
