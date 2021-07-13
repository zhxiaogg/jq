package com.github.zhxiaogg.jq.ast;

import com.github.zhxiaogg.jq.plan.logical.LogicalPlan;
import com.github.zhxiaogg.jq.plan.logical.Scan;
import lombok.Data;

import java.util.Optional;

public interface TableOrSubQuery extends AstNode {
    LogicalPlan toPlanNode();

    @Data
    class Table implements TableOrSubQuery {
        private final TableName tableName;
        private final TableAlias alias;

        public LogicalPlan toPlanNode() {
            String tableName = getTableName().getName();
            Optional<String> alias = Optional.ofNullable(getAlias()).map(TableAlias::getName);
            // TODO: use alias
            return new Scan(tableName);
        }
    }

    @Data
    class SubQuery implements TableOrSubQuery {
        private final Select subQuery;
        private final TableAlias alias;

        public  LogicalPlan toPlanNode() {
            LogicalPlan logicalPlan = getSubQuery().toPlanNode();
            // TODO: use alias
            Optional<String> alias = Optional.of(getAlias()).map(TableAlias::getName);
            return new com.github.zhxiaogg.jq.plan.logical.SubQuery(logicalPlan);
        }
    }

}
