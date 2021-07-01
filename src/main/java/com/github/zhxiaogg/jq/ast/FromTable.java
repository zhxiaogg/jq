package com.github.zhxiaogg.jq.ast;

import lombok.Data;

import java.util.List;

public interface FromTable extends AstNode {

    @Data
    class Joins implements FromTable {
        private final Join join;
    }

    @Data
    class TableOrSubQueries implements FromTable {
        private final List<TableOrSubQuery> tableOrSubQueries;
    }

}
