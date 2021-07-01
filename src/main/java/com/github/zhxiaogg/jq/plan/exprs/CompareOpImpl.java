package com.github.zhxiaogg.jq.plan.exprs;

public interface CompareOpImpl {
    <T extends Comparable<T>> boolean compareImpl(T left, T right);
}
