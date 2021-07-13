package com.github.zhxiaogg.jq.ast;

import com.github.zhxiaogg.jq.plan.exprs.BooleanExpression;
import com.github.zhxiaogg.jq.plan.exprs.Expression;
import com.github.zhxiaogg.jq.plan.logical.Aggregate;
import com.github.zhxiaogg.jq.plan.logical.Filter;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.plan.logical.Project;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class Select implements AstNode {
    private final List<ResultColumn> resultColumns;
    private final FromTable fromTable;
    private final Where where;
    private final GroupBy groupBy;

    public LogicalPlan toPlanNode() {
        LogicalPlan plan = null;
        if (fromTable != null) {
            plan = fromTable.toPlanNode();
        }

        if (where != null) {
            plan = new Filter((BooleanExpression) where.getExpr().toExpression(), plan);
        }

        List<Expression> projections = resultColumns.stream().map(ResultColumn::toExpression).collect(Collectors.toList());
        if (groupBy != null) {
            List<Expression> groupingKeys = groupBy.getGroupBys().stream().map(Expr::toExpression).collect(Collectors.toList());
            plan = new Aggregate(groupingKeys, projections, plan);
            if (groupBy.getHaving() != null) {
                plan = new Filter((BooleanExpression) groupBy.getHaving().getExpr().toExpression(), plan);
            }
        } else {
            plan = new Project(projections, plan);
        }
        return plan;
    }
}
