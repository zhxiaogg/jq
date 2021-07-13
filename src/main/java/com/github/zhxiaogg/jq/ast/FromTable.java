package com.github.zhxiaogg.jq.ast;

import com.github.zhxiaogg.jq.JoinType;
import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public interface FromTable extends AstNode {

    LogicalPlan toPlanNode();

    @Data
    class Joins implements FromTable {
        private final Join join;

        public LogicalPlan toPlanNode() {
            return join.toPlanNode();
        }
    }

    @Data
    class TableOrSubQueries implements FromTable {
        private final List<TableOrSubQuery> tableOrSubQueries;

        public LogicalPlan toPlanNode() {
            LogicalPlan plan = null;
            for (TableOrSubQuery tableOrSubQuery : tableOrSubQueries) {
                LogicalPlan p = tableOrSubQuery.toPlanNode();
                if (plan != null) {
                    plan = new com.github.zhxiaogg.jq.plan.logical.Join(plan, p, null, JoinType.INNER);
                } else {
                    plan = p;
                }
            }
            return plan;
        }
    }
}
