package com.jcv.hyperclean.util;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SetUtils {
    public static <T,R> Set<R> listToSet(List<T> list, Function<T,R> mapper) {
        if (list == null || mapper == null) {
            return Collections.emptySet();
        }

        return list.stream().map(mapper).collect(Collectors.toSet());
    }
}
