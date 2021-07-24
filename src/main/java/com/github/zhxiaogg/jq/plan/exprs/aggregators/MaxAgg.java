package com.github.zhxiaogg.jq.plan.exprs.aggregators;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.Max;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.plan.exprs.literals.LiteralImpl;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
public class MaxAgg extends AggExpression {
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
    public String toString() {
        return String.format("Max(%s)", child);
    }

    private static class MaxAggFunction implements AggregateFunction {
        private final Expression evaluate = new ResolvedAttribute("max", DataType.Int, 0);
        private final Expression max;
        private final Expression input;

        public MaxAggFunction(Expression input) {
            this.max = new Max(Arrays.asList(input, evaluate));
            this.input = input;
        }

        @Override
        public List<Expression> updateExpressions() {
            return Collections.singletonList(max);
        }

        @Override
        public List<Expression> initExpressions() {
            return Collections.singletonList(new LiteralImpl(getInitValue(input.getDataType()), input.getDataType()));
        }

        public Object getInitValue(DataType dataType) {
            switch (dataType) {
                case Float:
                    return Double.NEGATIVE_INFINITY;
                case Int:
                    return Long.MIN_VALUE;
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
            return AttributeSet.create(new ResolvedAttribute[]{evaluate.toAttribute()});
        }
    }
}
