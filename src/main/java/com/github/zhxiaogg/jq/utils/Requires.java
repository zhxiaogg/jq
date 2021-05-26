package com.github.zhxiaogg.jq.utils;

public class Requires {
    public static void require(boolean value, String message) {
        if (!value) throw new IllegalArgumentException(message);
    }
}
