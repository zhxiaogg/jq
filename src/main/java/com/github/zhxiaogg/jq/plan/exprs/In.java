package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class In implements NonLeafExprNode {
    private final Expression target;
    private final List<Expression> values;
    private final LogicalPlan subQuery;
    private final String id;

    public In(Expression target, List<Expression> values, LogicalPlan subQuery) {
        this(target, values, subQuery, UUID.randomUUID().toString());
    }

    @Override
    public boolean leafNode() {
        return false;
    }

    @Override
    public List<Expression> getChildren() {
        if (values != null) {
            ArrayList<Expression> result = new ArrayList<>();
            result.add(target);
            result.addAll(values);
            return result;
        } else {
            return Collections.singletonList(target);
        }
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        if (children.size() > 1) {
            return new In(children.get(0), children.subList(1, children.size()), null, id);
        } else {
            return new In(children.get(0), null, subQuery, id);
        }
    }

    @Override
    public Value eval(Record record) {
        return null;
    }

    @Override
    public DataType getDataType() {
        return DataType.Boolean;
    }
}
