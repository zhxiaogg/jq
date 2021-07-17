package com.github.zhxiaogg.jq.plan.exprs.aggregators;

import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exec.Record;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.Literal;
import com.github.zhxiaogg.jq.plan.exprs.ResolvedAttribute;
import com.github.zhxiaogg.jq.plan.exprs.math.Div;
import com.github.zhxiaogg.jq.plan.exprs.math.Plus;
import com.github.zhxiaogg.jq.schema.Attribute;
import com.github.zhxiaogg.jq.schema.DataType;
import com.github.zhxiaogg.jq.values.Value;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
public class AvgAgg extends AggExpr {
    public AvgAgg(Expression child, String id) {
        super(child, id, new AvgAggFunction(child));
    }

    public AvgAgg(Expression child) {
        super(child, UUID.randomUUID().toString(), new AvgAggFunction(child));
    }

    @Override
    public Expression withChildren(List<Expression> children) {
        return new AvgAgg(children.get(0), id);
    }

    @Override
    public Value eval(Record record) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public String toString() {
        return String.format("AVG(%s)", child);
    }

    private static class AvgAggFunction implements AggregateFunction {
        private final Expression sum;
        private final Expression count;
        private final Expression evaluate;

        public AvgAggFunction(Expression child) {
            ResolvedAttribute sumInput = new ResolvedAttribute("sum", child.getDataType(), 0);
            ResolvedAttribute countInput = new ResolvedAttribute("count", DataType.Int, 1);
            this.sum = new Plus(sumInput, child);
            this.count = new Plus(countInput, new Literal(1, DataType.Int));
            this.evaluate = new Div(sumInput, countInput);
        }

        @Override
        public List<Expression> updateExpressions() {
            return Arrays.asList(sum, count);
        }

        @Override
        public Expression evaluateExpression() {
            return evaluate;
        }

        @Override
        public AttributeSet updateOutputs() {
            return new AttributeSet(new Attribute[]{
                    this.sum.toAttribute(),
                    this.count.toAttribute()}
            );
        }
    }
}