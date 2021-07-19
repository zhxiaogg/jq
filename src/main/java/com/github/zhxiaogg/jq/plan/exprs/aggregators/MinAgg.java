package com.github.zhxiaogg.jq.plan.exprs.aggregators;

import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.Min;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.plan.exprs.literals.LiteralImpl;
import com.github.zhxiaogg.jq.schema.Attribute;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;
import com.github.zhxiaogg.jq.values.agg.MinAggValue;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
public class MinAgg extends AggExpression {
    public MinAgg(Expression child, String id) {
        super(child, id, new MinAggFunction(child));
    }

    public MinAgg(Expression child) {
        super(child, UUID.randomUUID().toString(), new MinAggFunction(child));
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new MinAgg(children.get(0), id);
    }

    @Override
    public Value eval(Record record) {
        LiteralValue v = (LiteralValue) child.eval(record);
        return new MinAggValue(v.getValue(), getDataType());
    }

    @Override
    public String toString() {
        return String.format("Min(%s)", child);
    }

    private static class MinAggFunction implements AggregateFunction {
        private final Expression evaluate = new ResolvedAttribute("min", DataType.Int, 0);
        private final Expression min;
        private final Expression input;

        public MinAggFunction(Expression input) {
            this.min = new Min(Arrays.asList(input, evaluate));
            this.input = input;
        }

        @Override
        public List<Expression> updateExpressions() {
            return Collections.singletonList(min);
        }

        @Override
        public List<Expression> initExpressions() {
            return Collections.singletonList(new LiteralImpl(getInitValue(input.getDataType()), input.getDataType()));
        }

        public Object getInitValue(DataType dataType) {
            switch (dataType) {
                case Float:
                    return Double.POSITIVE_INFINITY;
                case Int:
                    return Long.MAX_VALUE;
                default:
                    throw new IllegalStateException("unsupported data type " + dataType);
            }
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
