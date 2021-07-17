package com.github.zhxiaogg.jq.plan.exprs.aggregators;

import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.plan.exprs.Max;
import com.github.zhxiaogg.jq.schema.Attribute;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import com.github.zhxiaogg.jq.values.agg.MaxAggValue;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
public class MaxAgg extends AggExpr {
    public MaxAgg(Expression child, String id) {
        super(child, id, new MaxAggFunction(child));
    }

    public MaxAgg(Expression child) {
        super(child, UUID.randomUUID().toString(), new MaxAggFunction(child));
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new MaxAgg(children.get(0), id);
    }

    @Override
    public Value eval(Record record) {
        Value v = child.eval(record);
        return new MaxAggValue(v, getDataType());
    }

    @Override
    public String toString() {
        return String.format("Max(%s)", child);
    }

    private static class MaxAggFunction implements AggregateFunction {
        private final Expression evaluate = new ResolvedAttribute("max", DataType.Int, 0);
        private final Expression max;

        public MaxAggFunction(Expression input) {
            this.max = new Max(Arrays.asList(input, evaluate));
        }

        @Override
        public List<Expression> updateExpressions() {
            return Collections.singletonList(max);
        }

        @Override
        public Expression evaluateExpression() {
            return evaluate;
        }

        @Override
        public AttributeSet updateOutputs() {
            return new AttributeSet(new Attribute[]{max.toAttribute()});
        }
    }
}
