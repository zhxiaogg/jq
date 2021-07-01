package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.FromTable;
import com.github.zhxiaogg.jq.ast.TableOrSubQuery;

import java.util.ArrayList;
import java.util.List;

class TableOrSubQueriesBuilder implements FromTableBuilder<FromTable.TableOrSubQueries>, AcceptTableOrSubQuery {
    private final List<TableOrSubQuery> tableOrSubQueries = new ArrayList<>();

    @Override
    public FromTable.TableOrSubQueries build() {
        return new FromTable.TableOrSubQueries(tableOrSubQueries);
    }

    @Override
    public void accept(TableOrSubQuery node) {
        this.tableOrSubQueries.add(node);
    }
}
