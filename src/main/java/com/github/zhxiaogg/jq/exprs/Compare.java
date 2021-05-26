package com.github.zhxiaogg.jq.exprs;

import com.github.zhxiaogg.jq.plans.interpreter.Record;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class Compare implements BooleanExpression {
    private final CompareOp op;
    private final Expression left;
    private final Expression right;

    public Compare(CompareOp op, Expression left, Expression right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }


    @Override
    public boolean apply(Record record) {
        return op.compare(left.eval(record), right.eval(record));
    }
}
