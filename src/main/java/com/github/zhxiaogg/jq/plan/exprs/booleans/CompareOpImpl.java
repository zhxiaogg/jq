package com.github.zhxiaogg.jq.plan.exprs.booleans;

public interface CompareOpImpl {
    <T extends Comparable<T>> boolean compareImpl(T left, T right);
}
