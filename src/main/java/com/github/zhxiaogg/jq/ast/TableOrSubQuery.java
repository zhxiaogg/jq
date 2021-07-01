package com.github.zhxiaogg.jq.ast;

import lombok.Data;

public interface TableOrSubQuery extends AstNode {

    @Data
    class Table implements TableOrSubQuery {
        private final TableName tableName;
        private final TableAlias alias;
    }

    @Data
    class SubQuery implements TableOrSubQuery {
        private final Select subQuery;
        private final TableAlias alias;
    }

}
