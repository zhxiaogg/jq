package com.github.zhxiaogg.jq.utils;

import java.util.Objects;
import java.util.Optional;

public final class ObjectUtils {
    public static <T> Optional<T> firstNonNull(T... list) {
        for (T t : list) {
            if (Objects.nonNull(t)) return Optional.of(t);
        }
        return Optional.empty();
    }
}
