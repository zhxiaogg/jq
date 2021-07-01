package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.Node;
import com.github.zhxiaogg.jq.plan.logical.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;

public interface Expression extends Node<Expression> {

    Value eval(Record record);

    String getDisplayName();

    DataType getDataType();
}
