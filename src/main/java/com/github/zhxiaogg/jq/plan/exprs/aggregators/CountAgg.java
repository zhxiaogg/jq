package com.github.zhxiaogg.jq.plan.exprs.aggregators;

import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.Literal;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.plan.exprs.math.Plus;
import com.github.zhxiaogg.jq.schema.Attribute;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CountAgg extends AggExpr {
    public CountAgg(Expression child) {
        this(child, UUID.randomUUID().toString());
    }

    public CountAgg(Expression child, String id) {
        super(child, id, new CountAggFunction());
    }


    @Override
    public Expression withChildren(List<Expression> children) {
        return new CountAgg(children.get(0), id);
    }

    @Override
    public Value eval(Record record) {
        return null;
    }

    private static class CountAggFunction implements AggregateFunction {
        private final Expression evaluate = new ResolvedAttribute("count", DataType.Int, 0);
        private final Expression count = new Plus(evaluate, new Literal(0, DataType.Int));

        @Override
        public List<Expression> updateExpressions() {
            return Collections.singletonList(count);
        }

        @Override
        public Expression evaluateExpression() {
            return evaluate;
        }

        @Override
        public AttributeSet updateOutputs() {
            return new AttributeSet(new Attribute[]{evaluate.toAttribute()});
        }
    }
}
