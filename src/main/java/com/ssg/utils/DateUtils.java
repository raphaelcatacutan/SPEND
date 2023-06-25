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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class DateUtils {
    public static int calculateWeeksAgo(Timestamp timestamp) {
        Instant instant = timestamp.toInstant();
        Instant now = Instant.now();
        Duration duration = Duration.between(instant, now);
        return (int) duration.toDays() / 7;
    }

    public static int calculateWeeksAgo(Date date) {
        LocalDate localDate = date.toLocalDate();
        LocalDate now = LocalDate.now();
        Period period = Period.between(localDate, now);
        return period.getDays() / 7;
    }

    public static int calculateDaysAgo(Timestamp timestamp) {
        Instant instant = timestamp.toInstant();
        Instant now = Instant.now();
        Duration duration = Duration.between(instant, now);
        return (int) duration.toDays();
    }

    public static int calculateDaysAgo(Date date) {
        Date currentDate = new Date(System.currentTimeMillis());
        long diff = currentDate.getTime() - date.getTime();
        return (int) (diff / (24 * 60 * 60 * 1000));
    }

    public static int calculateMonthsAgo(Timestamp timestamp) {
        LocalDate localDate = timestamp.toLocalDateTime().toLocalDate();
        LocalDate now = LocalDate.now();
        Period period = Period.between(localDate, now);
        return period.getMonths();
    }

    public static int calculateMonthsAgo(java.sql.Date date) {
        LocalDate localDate = date.toLocalDate();
        LocalDate now = LocalDate.now();
        Period period = Period.between(localDate, now);
        return period.getMonths();
    }


    public static String[] getLastMonths(int monthNumbers) {
        Calendar cal = Calendar.getInstance();
        Date now = new Date(cal.getTimeInMillis());
        ArrayList<Date> lastMonths = new ArrayList<>();
        for (int i = 0; i < monthNumbers - 1; i++) {
            cal.add(Calendar.MONTH, -1);
            cal.set(Calendar.DATE, 1);
            Date date = new Date(cal.getTimeInMillis());
            lastMonths.add(date);
        }
        Collections.reverse(lastMonths);
        lastMonths.add(now);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM");
        String[] lastMonthsAsStrings = new String[lastMonths.size()];
        for (int i = 0; i < lastMonths.size(); i++) {
            lastMonthsAsStrings[i] = dateFormat.format(lastMonths.get(i));
        }

        return lastMonthsAsStrings;
    }

    public static String[] getLastWeeks(int weekNumbers) {
        Calendar cal = Calendar.getInstance();
        Date now = new Date(cal.getTimeInMillis());
        ArrayList<Date> lastWeeks = new ArrayList<>();
        for (int i = 0; i < weekNumbers - 1; i++) {
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            Date date = new Date(cal.getTimeInMillis());
            lastWeeks.add(date);
        }
        Collections.reverse(lastWeeks);
        lastWeeks.add(now);

        String[] lastWeeksAsStrings = new String[lastWeeks.size()];
        for (int i = 0; i < lastWeeks.size(); i++) {
            int weekNumber = cal.get(Calendar.WEEK_OF_YEAR);
            lastWeeksAsStrings[i] = weekNumber + "th week";
            cal.add(Calendar.WEEK_OF_YEAR, -1);
        }

        return lastWeeksAsStrings;
    }

    public static String[] getLastDays(int dayNumbers) {
        Calendar cal = Calendar.getInstance();
        Date now = new Date(cal.getTimeInMillis());
        ArrayList<Date> lastDays = new ArrayList<>();
        for (int i = 0; i < dayNumbers - 1; i++) {
            cal.add(Calendar.DAY_OF_YEAR, -1);
            Date date = new Date(cal.getTimeInMillis());
            lastDays.add(date);
        }
        Collections.reverse(lastDays);
        lastDays.add(now);

        String[] lastDaysAsStrings = new String[lastDays.size()];
        for (int i = 0; i < lastDays.size(); i++) {
            int dayNumber = cal.get(Calendar.DAY_OF_MONTH);
            lastDaysAsStrings[i] = dayNumber + "th day";
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }

        return lastDaysAsStrings;
    }

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
