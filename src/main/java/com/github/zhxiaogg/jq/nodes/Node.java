package com.github.zhxiaogg.jq.nodes;

import com.github.zhxiaogg.jq.analyzer.Rule;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.zhxiaogg.jq.utils.ListUtils.zipList;

public interface Node<T extends Node<T>> {
    boolean leafNode();

    List<T> getChildren();

    T withChildren(List<T> children);

    default Optional<T> transformUp(Rule<T> rule) {
        // apply rule on children
        List<T> children = this.getChildren();
        List<Optional<T>> list = children.stream().map(n -> n.transformUp(rule)).collect(Collectors.toList());

        // apply on self if possible
        if (list.stream().anyMatch(Optional::isPresent)) {
            List<T> updated = zipList(children, list);
            T newNode = this.withChildren(updated);
            newNode = rule.apply(newNode).orElse(newNode);
            return Optional.of(newNode);
        } else {
            return rule.apply((T) this);
        }
    }

    default Optional<T> transformDown(Rule<T> rule) {
        Optional<T> newNode = rule.apply((T) this);

        // apply rule on children
        T node = newNode.orElse((T) this);
        List<T> children = node.getChildren();
        List<Optional<T>> list = children.stream().map(n -> n.transformDown(rule)).collect(Collectors.toList());

        // return a new node if possible
        if (list.stream().anyMatch(Optional::isPresent)) {
            List<T> updated = zipList(children, list);
            return Optional.of(node.withChildren(updated));
        } else {
            return newNode;
        }

    }
}
