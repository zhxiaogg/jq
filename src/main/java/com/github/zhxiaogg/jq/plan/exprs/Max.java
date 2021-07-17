package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Max implements NonLeafExprNode {
    private final String id;
    private final List<Expression> children;

    public Max(String id, List<Expression> children) {
        this.children = children;
        this.id = id;
    }

    public Max(List<Expression> children) {
        this(UUID.randomUUID().toString(), children);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Max(id, children);
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
