package com.jcv.hyperclean.exception;

public class HCValidationFailedException extends Exception {

    public HCValidationFailedException() {
        super("Error: Excepción de tipo HyperCleanInvalidException.");
    }

    public HCValidationFailedException(String message) {
        super(message);
    }

    public HCValidationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public HCValidationFailedException(Throwable cause) {
        super(cause);
    }
}
