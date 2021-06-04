package com.github.zhxiaogg.jq.nodes.exprs;

import com.github.zhxiaogg.jq.nodes.Node;
import com.github.zhxiaogg.jq.nodes.logical.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;

public interface Expression extends Node<Expression> {

    Value eval(Record record);

    String getDisplayName();

    DataType getDataType();
}
