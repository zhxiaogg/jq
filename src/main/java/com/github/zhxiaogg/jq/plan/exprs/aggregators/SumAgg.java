package com.github.zhxiaogg.jq.plan.exprs.aggregators;

import com.github.zhxiaogg.jq.datatypes.DataType;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.plan.exprs.literals.LiteralImpl;
import com.github.zhxiaogg.jq.plan.exprs.math.Plus;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
public class SumAgg extends AggExpression {
    public SumAgg(Expression child, String id) {
        super(child, id, new SumAggFunction(child));
    }

    public SumAgg(Expression child) {
        super(child, UUID.randomUUID().toString(), new SumAggFunction(child));
    }

    @Override
    public String toString() {
        return "Sum(" + child + ")";
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new SumAgg(children.get(0), id);
    }

    private static class SumAggFunction implements AggregateFunction {
        private final Expression plus;
        private final Expression evaluate;
        private final Expression input;

        public SumAggFunction(Expression input) {
            ResolvedAttribute result = new ResolvedAttribute("sum", input.getDataType(), 0);
            this.plus = new Plus(result, input);
            this.evaluate = result;
            this.input = input;
        }

        @Override
        public List<Expression> initExpressions() {
            return Collections.singletonList(new LiteralImpl(getInitValue(input.getDataType()), input.getDataType()));
        }

        public Object getInitValue(DataType dataType) {
            switch (dataType) {
                case Float:
                    return 0.0D;
                case Int:
                    return 0L;
                case Any:
                    // this may happen with UnStructuralRelations, like JSON objects, we treat
                    // the numbers as double here.
                    // TODO: improve this?
                    return 0.0D;
                default:
                    throw new IllegalStateException("unsupported data type " + dataType);
            }
        }

        @Override
        public List<Expression> updateExpressions() {
            return Collections.singletonList(plus);
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
