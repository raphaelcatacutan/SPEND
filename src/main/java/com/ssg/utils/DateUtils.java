package com.ssg.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    /**
     * Calculates the number of weeks between the specified timestamp and the current time.
     *
     * @param timestamp The timestamp to calculate the weeks ago from.
     * @return The number of weeks ago as an integer value.
     */
    public static int calculateWeeksAgo(Timestamp timestamp) {
        Instant instant = timestamp.toInstant();
        Instant now = Instant.now();
        Duration duration = Duration.between(instant, now);
        return (int) duration.toDays() / 7;
    }

    /**
     * Calculates the number of weeks between the specified date and the current date.
     *
     * @param date The date to calculate the weeks ago from.
     * @return The number of weeks ago as an integer value.
     */
    public static int calculateWeeksAgo(Date date) {
        LocalDate localDate = date.toLocalDate();
        LocalDate now = LocalDate.now();
        Period period = Period.between(localDate, now);
        return period.getDays() / 7;
    }

    /**
     * Calculates the number of days between the specified timestamp and the current time.
     *
     * @param timestamp The timestamp to calculate the days ago from.
     * @return The number of days ago as an integer value.
     */
    public static int calculateDaysAgo(Timestamp timestamp) {
        Instant instant = timestamp.toInstant();
        Instant now = Instant.now();
        Duration duration = Duration.between(instant, now);
        return (int) duration.toDays();
    }

    /**
     * Calculates the number of days between the specified date and the current date.
     *
     * @param date The date to calculate the days ago from.
     * @return The number of days ago as an integer value.
     */
    public static int calculateDaysAgo(Date date) {
        Date currentDate = new Date(System.currentTimeMillis());
        long diff = currentDate.getTime() - date.getTime();
        return (int) (diff / (24 * 60 * 60 * 1000));
    }

    /**
     * Calculates the number of months between the specified timestamp and the current timestamp.
     *
     * @param timestamp The timestamp to calculate the months ago from.
     * @return The number of months ago as an integer value.
     */
    public static int calculateMonthsAgo(Timestamp timestamp) {
        LocalDate localDate = timestamp.toLocalDateTime().toLocalDate();
        LocalDate now = LocalDate.now();
        Period period = Period.between(localDate, now);
        return period.getMonths();
    }

    /**
     * Calculates the number of months between the specified date and the current date.
     *
     * @param date The date to calculate the months ago from.
     * @return The number of months ago as an integer value.
     */
    public static int calculateMonthsAgo(java.sql.Date date) {
        LocalDate localDate = date.toLocalDate();
        LocalDate now = LocalDate.now();
        Period period = Period.between(localDate, now);
        return period.getMonths();
    }

    /**
     * Retrieves the year from the specified date.
     *
     * @param date The date from which to retrieve the year.
     * @return The year as an integer value.
     */
    public static int getYear(Date date) {
        LocalDate localDate = date.toLocalDate();
        return localDate.getYear();
    }

    /**
     * Converts a Date to String
     *
     * @param format The string format. Use Quick Formats by passing numbers
     * @param date   The date to format
     * @return The string format of the date
     */
    public static String formatDate(String format, Date date) {
        format = switch (format) {
            // Quick Formats
            case "1" -> "yyyy-MMM-dd";
            case "2" -> "MMMM dd, yyyy";
            case "3" -> "yyyy-MM-dd";
            case "4" -> "yyyy-MM-dd HH:mm:ss";
            case "5" -> "yyyy";
            case "6" -> "MMMM";
            case "7" -> "MMM";
            default -> format;
        };
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = date.toLocalDate();
        return formatter.format(localDate);
    }

    /**
     * Converts a String to Date
     *
     * @param format The string format. Use Quick Formats by passing numbers
     * @param date   The string to parse
     * @return The parsed date
     */
    public static Date parseDate(String format, String date) {
        try {
            format = switch (format) {
                // Quick Formats
                case "1" -> "yyyy-MMM-dd";
                case "2" -> "MMMM dd, yyyy";
                case "3" -> "yyyy-MM-dd";
                case "4" -> "yyyy-MM-dd HH:mm:ss";
                default -> format;
            };
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return new Date(formatter.parse(date).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }



}
