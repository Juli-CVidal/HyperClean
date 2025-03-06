package com.jcv.hyperclean.exception;

import lombok.Getter;

import static com.jcv.hyperclean.util.DateUtils.TIME_FORMAT;

@Getter
public class HCInvalidDateTimeFormat extends Exception {
    private final String invalidDate;

    public HCInvalidDateTimeFormat(String invalidDate) {
        super(String.format("Invalid date format. Expected format: %s", TIME_FORMAT));
        this.invalidDate = invalidDate;
    }
}
