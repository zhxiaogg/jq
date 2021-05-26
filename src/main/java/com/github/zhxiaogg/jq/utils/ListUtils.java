package com.github.zhxiaogg.jq.utils;

import com.github.zhxiaogg.jq.nodes.exprs.Expression;
import com.github.zhxiaogg.jq.schema.Attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ListUtils {
    public static <T> List<T> zipList(List<T> children, List<Optional<T>> list) {
        List<T> updated = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            updated.add(list.get(i).isPresent() ? list.get(i).get() : children.get(i));
        }
        return updated;
    }

    public static <T> List<T> concat(List<T> left, List<T> right) {
        ArrayList<T> result = new ArrayList<>(left.size() + right.size());
        result.addAll(left);
        result.addAll(right);
        return result;
    }
}
