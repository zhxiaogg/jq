package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class Between implements NonLeafExprNode {
    private final Expression target;
    private final Expression left;
    private final Expression right;
    private final String id;

    public Between(Expression target, Expression left, Expression right) {
        this(target, left, right, UUID.randomUUID().toString());
    }

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<Expression> getChildren() {
        return Arrays.asList(target, left, right);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new Between(children.get(0), children.get(1), children.get(2), id);
    }

    @Override
    public Value eval(Record record) {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public DataType getDataType() {
        return DataType.Boolean;
    }
}
