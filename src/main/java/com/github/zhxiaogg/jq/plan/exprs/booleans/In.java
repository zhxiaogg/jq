package com.github.zhxiaogg.jq.plan.exprs.booleans;

import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.NonLeafExprNode;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.utils.ListUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;
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
    public boolean semanticEqual(Expression other) {
        return other instanceof In &&
                (ListUtils.zip(values, ((In) other).values).stream().allMatch(p -> p.getLeft().semanticEqual(p.getRight())) ||
                        Objects.equals(subQuery, ((In) other).subQuery));
    }

    @Override
    public Boolean evaluate(Record record) {
        List<Object> values = this.values.stream().map(e -> e.evaluate(record)).collect(Collectors.toList());
        Object targetValue = target.evaluate(record);
        return values.contains(targetValue);
    }
}
