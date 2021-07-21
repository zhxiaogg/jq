package com.github.zhxiaogg.jq.utils;

import com.github.zhxiaogg.jq.schema.DataType;

public interface BinaryValueOp<T> {

    Object applyWithDataType(DataType dataType, Object l, Object r);
}
