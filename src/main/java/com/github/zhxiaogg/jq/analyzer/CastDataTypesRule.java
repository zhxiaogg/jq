package com.github.zhxiaogg.jq.analyzer;

import com.github.zhxiaogg.jq.plan.exprs.Cast;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.exprs.FunctionCall;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.schema.DataType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Add {@link Cast} wherever data type casting is needed.
 * <p>
 * Note that a later stage in the optimizer is supposed to be responsible for finding the data type inconsistency,
 * so it's not a MUST for the implementation to perform all the cast.
 *
 * @return A new expression if type casting is needed.
 */
public class CastDataTypesRule implements Rule<LogicalPlan> {

    private static class CastExprDataTypes implements Rule<Expression> {
        @Override
        public Optional<Expression> apply(Expression e) {
            return e.transformUp(n -> {
                if (n.leafNode() || n instanceof FunctionCall) {
                    return Optional.empty();
                } else {
                    return ensureTypeConsistency(n.getChildren()).map(n::withChildren);
                }
            });
        }
    }

    @Override
    public Optional<LogicalPlan> apply(LogicalPlan node) {
        CastExprDataTypes castExprDataTypesRule = new CastExprDataTypes();
        return node.transformUp(n -> n.transformExpressionsUp(castExprDataTypesRule));
    }

    private static Optional<List<Expression>> ensureTypeConsistency(List<Expression> expressions) {
        Optional<List<Expression>> result;
        if (expressions.isEmpty() || expressions.size() == 1) {
            result = Optional.empty();
        } else {
            DataType candidate = expressions.get(0).getDataType();
            final DataType castTo = expressions.stream().reduce(candidate, (dt, expr) -> {
                DataType dataType = expr.getDataType();
                if (dataType.equals(dt) || dataType.canCastTo(dt)) {
                    return dt;
                } else if (dt.canCastTo(dataType)) {
                    return dataType;
                } else {
                    return dt;
                }
            }, (dt1, dt2) -> {
                if (dt1.equals(dt2)) {
                    return dt1;
                } else if (dt1.canCastTo(dt2)) {
                    return dt2;
                } else if (dt2.canCastTo(dt1)) {

                    return dt1;
                } else {
                    return dt1;
                }
            });

            if (expressions.stream().allMatch(e -> e.getDataType().equals(castTo))) {
                result = Optional.empty();
            } else {
                result = Optional.of(expressions.stream().map(e -> {
                    if (e.getDataType().canCastTo(castTo)) {
                        return new Cast(e, castTo);
                    } else {
                        return e;
                    }
                }).collect(Collectors.toList()));
            }
        }
        return result;
    }

}
