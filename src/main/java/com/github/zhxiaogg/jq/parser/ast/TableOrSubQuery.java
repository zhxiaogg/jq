package com.github.zhxiaogg.jq.parser.ast;

import lombok.Data;

public interface TableOrSubQuery extends AstNode {

    interface AcceptTableOrSubQuery {
        void accept(TableOrSubQuery node);
    }

    interface TableOrSubQueryBuilder<T extends TableOrSubQuery> extends AstBuilder<T> {
        @Override
        T build();
    }

    @Data
    class Table implements TableOrSubQuery {
        private final TableName tableName;
        private final TableAlias alias;
    }

    class TableBuilder implements TableOrSubQueryBuilder<Table>, TableName.AcceptTableName, TableAlias.AcceptTableAlias {
        private TableName tableName;
        private TableAlias alias;

        @Override
        public Table build() {
            return new Table(tableName, alias);
        }

        @Override
        public void accept(TableAlias tableAlias) {
            this.alias = tableAlias;
        }

        @Override
        public void accept(TableName tableName) {
            this.tableName = tableName;
        }
    }

    @Data
    class SubQuery implements TableOrSubQuery {
        private final Select subQuery;
        private final TableAlias alias;
    }

    class SubQueryBuilder implements TableOrSubQueryBuilder<SubQuery>, Select.AcceptSelect, TableAlias.AcceptTableAlias {
        private Select subQuery;
        private TableAlias alias;

        @Override
        public void accept(Select select) {
            this.subQuery = select;
        }

        @Override
        public SubQuery build() {
            return new SubQuery(subQuery, alias);
        }

        @Override
        public void accept(TableAlias tableAlias) {
            this.alias = tableAlias;
        }
    }
}
