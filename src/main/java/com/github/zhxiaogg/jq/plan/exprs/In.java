package com.github.zhxiaogg.jq.plan.exprs;

import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.plan.logical.interpreter.Record;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class In implements Expression {
    private final Expression target;
    private final List<Expression> values;
    private final LogicalPlan subQuery;

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
            return new In(children.get(0), children.subList(1, children.size()), null);
        } else {
            return new In(children.get(0), null, subQuery);
        }
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
