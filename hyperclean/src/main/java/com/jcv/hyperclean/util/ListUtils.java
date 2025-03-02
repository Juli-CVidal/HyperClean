package com.jcv.hyperclean.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListUtils {
    public static <T, R> List<R> mapList(List<T> list, Function<T, R> mapper) {
        if (list == null || mapper == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    public static <T, K, V> Map<K, V> listToMap(List<T> list, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        if (list == null || keyMapper == null || valueMapper == null) {
            return Collections.emptyMap();
        }
        return list.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }
}
