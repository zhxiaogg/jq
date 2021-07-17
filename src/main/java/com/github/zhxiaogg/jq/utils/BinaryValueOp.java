package com.github.zhxiaogg.jq.utils;

import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;

public interface BinaryValueOp<T> {
    default T apply(Value left, Value right) {
        if (left.getDataType() == right.getDataType()) {
            return applyWithDataType(left.getDataType(), left.getValue(), right.getValue());
        } else {
            String msg = String.format("cannot run with different data types, left: %s, right: %s.", left.getDataType(), right.getDataType());
            throw new IllegalArgumentException(msg);
        }
    }

    T applyWithDataType(DataType dataType, Object l, Object r);
}
