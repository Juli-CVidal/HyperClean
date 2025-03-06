package com.jcv.hyperclean.exception;

import lombok.Getter;

@Getter
public class HCValidationFailedException extends Exception {
    private final Object entity;

    public HCValidationFailedException(Object entity, String message) {
        super(message);
        this.entity = entity;
    }
}