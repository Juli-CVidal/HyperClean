package com.jcv.hyperclean.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter dd_MM_yyyy_HH_mm_ss = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static LocalDateTime convertStringToLocalDateTime(String dateStr) {
        return LocalDateTime.parse(dateStr, dd_MM_yyyy_HH_mm_ss);
    }
}
