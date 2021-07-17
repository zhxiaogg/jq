package com.github.zhxiaogg.jq.plan.exprs.aggregators;

import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exprs.Expression;

import java.util.List;

public interface AggregateFunction {
    List<Expression> updateExpressions();

    default List<Expression> mergeExpressions() {
        throw new IllegalStateException("not supported yet!");
    }

    Expression evaluateExpression();

    /**
     * @return Outputs shape for the updateExpressions.
     */
    AttributeSet updateOutputs();
}
