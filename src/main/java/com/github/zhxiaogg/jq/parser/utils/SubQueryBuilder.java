package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.Select;
import com.github.zhxiaogg.jq.ast.TableAlias;
import com.github.zhxiaogg.jq.ast.TableOrSubQuery;

class SubQueryBuilder implements TableOrSubQueryBuilder<TableOrSubQuery.SubQuery>, AcceptSelect, AcceptTableAlias {
    private Select subQuery;
    private TableAlias alias;

    @Override
    public void accept(Select select) {
        this.subQuery = select;
    }

    @Override
    public TableOrSubQuery.SubQuery build() {
        return new TableOrSubQuery.SubQuery(subQuery, alias);
    }

    @Override
    public void accept(TableAlias tableAlias) {
        this.alias = tableAlias;
    }
}
