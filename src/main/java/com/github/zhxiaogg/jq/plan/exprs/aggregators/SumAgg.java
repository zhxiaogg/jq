package com.github.zhxiaogg.jq.plan.exprs.aggregators;

import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.plan.exprs.math.Plus;
import com.github.zhxiaogg.jq.schema.Attribute;
import com.github.zhxiaogg.jq.values.LiteralValue;
import com.github.zhxiaogg.jq.values.Value;
import com.github.zhxiaogg.jq.values.agg.SumAggValue;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
public class SumAgg extends AggExpr {
    public SumAgg(Expression child, String id) {
        super(child, id, new SumAggFunction(child));
    }

    public SumAgg(Expression child) {
        super(child, UUID.randomUUID().toString(), new SumAggFunction(child));
    }

    @Override
    public Value eval(Record record) {
        LiteralValue value = (LiteralValue) child.eval(record);
        return SumAggValue.from(value);
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

        public SumAggFunction(Expression target) {
            ResolvedAttribute result = new ResolvedAttribute("sum", target.getDataType(), 0);
            this.plus = new Plus(result, target);
            this.evaluate = result;
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
            return new AttributeSet(new Attribute[]{evaluate.toAttribute()});
        }
    }
}
