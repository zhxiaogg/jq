package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.TableAlias;
import com.github.zhxiaogg.jq.ast.TableName;
import com.github.zhxiaogg.jq.ast.TableOrSubQuery;

class TableBuilder implements TableOrSubQueryBuilder<TableOrSubQuery.Table>, AcceptTableName, AcceptTableAlias {
    private TableName tableName;
    private TableAlias alias;

    @Override
    public TableOrSubQuery.Table build() {
        return new TableOrSubQuery.Table(tableName, alias);
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
