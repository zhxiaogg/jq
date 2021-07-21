package com.github.zhxiaogg.jq.plan.exec;

import java.util.List;

public interface Record {

    static Record create(List<Object> values) {
        return new SimpleRecord(values);
    }

    Object indexOf(int ordinal);

    /**
     * @return num values.
     */
    int size();

    default boolean getBoolean(int ordinal) {
        return (boolean) indexOf(ordinal);
    }

    MutableRecord toMutable();
}
