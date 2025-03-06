package com.jcv.hyperclean.exception;

import lombok.Getter;

@Getter
public class HCNotFoundException extends RuntimeException {
    public HCNotFoundException(String message) {
        super(message);
    }
}