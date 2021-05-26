package com.github.zhxiaogg.jq.analyzer;

import com.github.zhxiaogg.jq.nodes.Node;

import java.util.Optional;

@FunctionalInterface
public interface Rule<T extends Node<T>> {
    Optional<T> apply(T node);

}
