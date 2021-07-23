package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.Node;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;

public interface Expression extends Node<Expression> {

    default ResolvedAttribute toAttribute() {
        // TODO: don't use toString here
        return new ResolvedAttribute(getId(), toString(), getDataType(), -1);
    }

    boolean semanticEqual(Expression other);

    boolean isResolved();

    String getId();

    Object evaluate(Record record);

    DataType getDataType();
}
