package com.github.zhxiaogg.jq.parser.ast;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public interface FromTable extends AstNode {
    interface AcceptFromTable {
        void accept(FromTable fromTable);
    }

    interface FromTableBuilder<T extends FromTable> extends AstBuilder<T> {
        @Override
        T build();
    }

    @Data
    class Joins implements FromTable {
        private final Join join;
    }

    class JoinsBuilder implements FromTableBuilder<Joins>, Join.AcceptJoin {
        private Join join;

        @Override
        public Joins build() {
            return new Joins(join);
        }

        @Override
        public void accept(Join join) {
            this.join = join;
        }
    }

    @Data
    class TableOrSubQueries implements FromTable {
        private final List<TableOrSubQuery> tableOrSubQueries;
    }

    class TableOrSubQueriesBuilder implements FromTableBuilder<TableOrSubQueries>, TableOrSubQuery.AcceptTableOrSubQuery {
        private final List<TableOrSubQuery> tableOrSubQueries = new ArrayList<>();

        @Override
        public TableOrSubQueries build() {
            return new TableOrSubQueries(tableOrSubQueries);
        }

        @Override
        public void accept(TableOrSubQuery node) {
            this.tableOrSubQueries.add(node);
        }
    }
}
