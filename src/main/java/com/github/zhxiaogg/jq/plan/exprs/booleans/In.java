package com.github.zhxiaogg.jq.plan.exprs.booleans;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.NonLeafExprNode;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.values.Value;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class In implements NonLeafExprNode, BooleanExpression {
    private final Expression target;
    private final List<Expression> values;
    private final LogicalPlan subQuery;
    private final String id;

    public In(Expression target, List<Expression> values, LogicalPlan subQuery) {
        this(target, values, subQuery, UUID.randomUUID().toString());
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
    public boolean apply(Record record) {
        List<Value> values = this.values.stream().map(e -> e.eval(record)).collect(Collectors.toList());
        Value targetValue = target.eval(record);
        return values.contains(targetValue);
    }
}
