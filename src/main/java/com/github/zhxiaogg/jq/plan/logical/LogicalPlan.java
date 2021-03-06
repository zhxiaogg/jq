package com.github.zhxiaogg.jq.plan.logical;

import com.github.zhxiaogg.jq.catalog.Catalog;
import com.github.zhxiaogg.jq.analyzer.Rule;
import com.github.zhxiaogg.jq.plan.Node;
import com.github.zhxiaogg.jq.plan.exec.AttributeSet;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.utils.ListUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface LogicalPlan extends Node<LogicalPlan> {

    LogicalPlan withExpressions(List<Expression> expressions);

    List<Expression> getExpressions();

    AttributeSet outputs(Catalog catalog);

    default Optional<LogicalPlan> transformExpressionsUp(Rule<Expression> rule) {
        List<Optional<Expression>> optExpressions = getExpressions().stream().map(expr -> expr.transformUp(rule)).collect(Collectors.toList());

        if (optExpressions.stream().anyMatch(Optional::isPresent)) {
            List<Expression> ts = ListUtils.zipAndPatch(getExpressions(), optExpressions);
            return Optional.of(withExpressions(ts));
        } else {
            return Optional.empty();
        }
    }

    default Optional<LogicalPlan> transformExpressionsDown(Rule<Expression> rule) {
        List<Optional<Expression>> optExpressions = getExpressions().stream().map(expr -> expr.transformDown(rule)).collect(Collectors.toList());

        if (optExpressions.stream().anyMatch(Optional::isPresent)) {
            List<Expression> ts = ListUtils.zipAndPatch(getExpressions(), optExpressions);
            return Optional.of(withExpressions(ts));
        } else {
            return Optional.empty();
        }
    }
}
