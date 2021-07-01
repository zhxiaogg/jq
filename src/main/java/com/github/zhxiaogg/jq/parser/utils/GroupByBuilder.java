package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Expr;
import com.github.zhxiaogg.jq.ast.GroupBy;
import com.github.zhxiaogg.jq.ast.Having;

import java.util.ArrayList;
import java.util.List;

class GroupByBuilder implements AstBuilder<GroupBy>, AcceptExpr, AcceptHaving {
    private final List<Expr> groupBys = new ArrayList<>();
    private Having having;

    @Override
    public GroupBy build() {
        return new GroupBy(groupBys, having);
    }

    @Override
    public void accept(Expr expr) {
        groupBys.add(expr);
    }

    @Override
    public void accept(Having having) {
        this.having = having;
    }
}
