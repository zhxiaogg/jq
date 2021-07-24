package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.ColumnName;
import com.github.zhxiaogg.jq.ast.Expr;

import java.util.ArrayList;
import java.util.List;

class ExprColumnRefBuilder implements ExprBuilder<Expr.ExprColumnRef>, AcceptColumnName {
    private final List<ColumnName> columnNames = new ArrayList<>();

    @Override
    public Expr.ExprColumnRef build() {
        return new Expr.ExprColumnRef(columnNames);
    }

    @Override
    public void accept(ColumnName columnName) {
        this.columnNames.add(columnName);
    }
}
