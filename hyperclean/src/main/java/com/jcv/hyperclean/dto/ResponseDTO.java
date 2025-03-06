package com.jcv.hyperclean.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {
    private T data;
    private String message;

    public static <T> ResponseDTO<T> of(T data, String message) {
        return new ResponseDTO<>(data, message);
    }
}