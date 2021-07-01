package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.ResultColumn;

class ResultColumnTableStarBuilder implements ResultColumnBuilder<ResultColumn.ResultColumnTableStar> {
    private final String tableName;

    public ResultColumnTableStarBuilder(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public ResultColumn.ResultColumnTableStar build() {
        return new ResultColumn.ResultColumnTableStar(tableName);
    }
}
