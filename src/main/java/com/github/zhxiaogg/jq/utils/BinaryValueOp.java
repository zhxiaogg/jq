package com.github.zhxiaogg.jq.utils;

import com.github.zhxiaogg.jq.datatypes.DataType;

public interface BinaryValueOp<T> {

    Object applyWithDataType(DataType dataType, Object l, Object r);
}
