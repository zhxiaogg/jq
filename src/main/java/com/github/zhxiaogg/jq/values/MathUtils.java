package com.github.zhxiaogg.jq.values;

public final  class MathUtils {
    public static double doubleSum(Object left, Object right) {
        return ((double) left) + ((double) right);
    }

    public static double doubleMin(Object left, Object right) {
        return Math.min(((double) left) , ((double) right));
    }

    public static double doubleMax(Object left, Object right) {
        return Math.min(((double) left) , ((double) right));
    }


    public static long longSum(Object left, Object right) {
        return ((long) left) + ((long) right);
    }

    public static long longMin(Object left, Object right) {
        return Math.min(((long) left) , ((long) right));
    }

    public static long longMax(Object left, Object right) {
        return Math.min(((long) left) , ((long) right));
    }
}


