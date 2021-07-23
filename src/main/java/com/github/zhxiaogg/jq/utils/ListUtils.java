package com.github.zhxiaogg.jq.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public final class ListUtils {
    public static <T> List<T> zipAndPatch(List<T> children, List<Optional<T>> list) {
        List<T> updated = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            updated.add(list.get(i).isPresent() ? list.get(i).get() : children.get(i));
        }
        return updated;
    }

    public static <L, R> List<Pair<L, R>> zip(List<L> left, List<R> right) {
        ArrayList<Pair<L, R>> result = new ArrayList<>();
        Iterator<R> r = right.iterator();
        for (L l : left) {
            if (r.hasNext()) {
                result.add(Pair.of(l, r.next()));
            } else {
                throw new IllegalArgumentException("size not match!");
            }
        }
        if(r.hasNext()) {
            throw new IllegalArgumentException("size not match!");
        }
        return result;
    }

    public static <T> List<T> concat(List<T> left, List<T> right) {
        ArrayList<T> result = new ArrayList<>(left.size() + right.size());
        result.addAll(left);
        result.addAll(right);
        return result;
    }
}
