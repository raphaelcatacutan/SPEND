package com.ssg._test;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;

public class WeeksAgoCalculator {
    public static long calculateWeeksAgo(Timestamp timestamp) {
        Instant instant = timestamp.toInstant();
        Instant now = Instant.now();
        Duration duration = Duration.between(instant, now);
        return duration.toDays() / 7;
    }

    public static long calculateWeeksAgo(Date date) {
        LocalDate localDate = date.toLocalDate();
        LocalDate now = LocalDate.now();
        Period period = Period.between(localDate, now);
        return period.getDays() / 7;
    }
}
