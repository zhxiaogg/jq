package com.github.zhxiaogg.jq.plan.exprs.aggregators;

import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exprs.Expression;

import java.util.List;

public interface AggregateFunction {

    List<Expression> updateExpressions();

    /**
     * @return an {@link Expression} that will be used to initialize a {@link com.github.zhxiaogg.jq.plan.exec.Record}
     * which will be used to store intermediate aggregation results.
     */
    List<Expression> initExpressions();

    default List<Expression> mergeExpressions() {
        throw new IllegalStateException("not supported yet!");
    }

    Expression evaluateExpression();

    /**
     * @return Outputs shape for the updateExpressions.
     */
    AttributeSet updateOutputs();
}
