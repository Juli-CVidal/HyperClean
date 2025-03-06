package com.jcv.hyperclean.exception;

public class HCInvalidDateTimeFormat extends Exception {

    public HCInvalidDateTimeFormat() {
        super("Invalid date format, please use dd_MM_yyyy_HH_mm_ss");
    }

    public HCInvalidDateTimeFormat(String message) {
        super(message);
    }

    public HCInvalidDateTimeFormat(String message, Throwable cause) {
        super(message, cause);
    }

    public HCInvalidDateTimeFormat(Throwable cause) {
        super(cause);
    }
}
