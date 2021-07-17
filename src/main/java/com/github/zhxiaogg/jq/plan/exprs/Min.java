package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Min implements NonLeafExprNode {
    private final List<Expression> children;
    private final String id;

    public Min(List<Expression> children, String id) {
        this.children = children;
        this.id = id;
    }

    public Min(List<Expression> children) {
        this(children, UUID.randomUUID().toString());
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Min(children, id);
    }

    @Override
    public Value eval(Record record) {
        return null;
    }

    @Override
    public DataType getDataType() {
        return null;
    }
}

