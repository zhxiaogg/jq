package com.github.zhxiaogg.jq.plan.exprs.booleans;

import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.NonLeafExprNode;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class NotExpr implements NonLeafExprNode {
    private final Expression expr;
    private final String id;

    public NotExpr(Expression expr) {
        this(expr, UUID.randomUUID().toString());
    }

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
