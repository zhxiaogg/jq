package com.github.zhxiaogg.jq.utils;

import lombok.Data;

@Data
public final class Pair<L, R> {
    private final L left;
    private final R right;

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }
}
