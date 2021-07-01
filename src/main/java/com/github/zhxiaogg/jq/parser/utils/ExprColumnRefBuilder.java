package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.ColumnName;
import com.github.zhxiaogg.jq.ast.Expr;
import com.github.zhxiaogg.jq.ast.TableName;

class ExprColumnRefBuilder implements ExprBuilder<Expr.ExprColumnRef>, AcceptColumnName, AcceptTableName {
    private TableName tableName;
    private ColumnName columnName;

    @Override
    public Expr.ExprColumnRef build() {
        return new Expr.ExprColumnRef(tableName, columnName);
    }

    @Override
    public void accept(ColumnName columnName) {
        this.columnName = columnName;
    }

    @Override
    public void accept(TableName tableName) {
        this.tableName = tableName;
    }
}
