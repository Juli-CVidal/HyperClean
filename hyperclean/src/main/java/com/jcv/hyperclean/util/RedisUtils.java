package com.jcv.hyperclean.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.LinkedHashMap;

public class RedisUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static <T> T convertFromMap(Object object, Class<T> clazz) {
        if (object instanceof LinkedHashMap) {
            return objectMapper.convertValue(object, clazz);
        }
        return clazz.cast(object);
    }
}