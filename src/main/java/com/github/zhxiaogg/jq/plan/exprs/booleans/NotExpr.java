package com.github.zhxiaogg.jq.plan.exprs.booleans;

import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.logical.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class NotExpr implements Expression {
    private final Expression expr;

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<Expression> getChildren() {
        return Collections.singletonList(expr);
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new NotExpr(children.get(0));
    }

    @Override
    public Value eval(Record record) {
        return null;
    }

    @Override
    public String toString() {
        return String.format("NOT %s", expr);
    }

    @Override
    public DataType getDataType() {
        return null;
    }


}
