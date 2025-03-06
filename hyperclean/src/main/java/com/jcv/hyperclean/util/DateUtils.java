package com.jcv.hyperclean.util;

import com.jcv.hyperclean.exception.HCInvalidDateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {

    private static final DateTimeFormatter dd_MM_yyyy_HH_mm_ss = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static LocalDateTime stringToLocalDateTime(String dateStr) throws HCInvalidDateTimeFormat {
        try {
            return LocalDateTime.parse(dateStr, dd_MM_yyyy_HH_mm_ss);
        } catch (DateTimeParseException e) {
            throw new HCInvalidDateTimeFormat("Invalid date format. Expected format: dd-MM-yyyy HH:mm:ss", e);
        }
    }

    public static String localDateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(dd_MM_yyyy_HH_mm_ss);
    }

    public static String now(){
        return LocalDateTime.now().format(dd_MM_yyyy_HH_mm_ss);
    }
}
