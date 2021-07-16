package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.Node;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;

public interface Expression extends Node<Expression> {

    boolean isResolved();

    String getId();

    Value eval(Record record);

    DataType getDataType();
}
