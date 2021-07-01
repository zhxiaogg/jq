package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.*;

import java.util.ArrayList;
import java.util.List;

class SelectBuilder implements AstBuilder<Select>, AcceptResultColumn, AcceptFromTable, AcceptWhere, AcceptGroupBy {
    private final List<ResultColumn> resultColumns = new ArrayList<>();
    private FromTable fromTable;
    private Where where;
    private GroupBy groupBy;

    @Override
    public void accept(ResultColumn resultColumn) {
        resultColumns.add(resultColumn);
    }

    @Override
    public void accept(FromTable fromTable) {
        this.fromTable = fromTable;
    }

    @Override
    public Select build() {
        return new Select(resultColumns, fromTable, where, groupBy);
    }

    @Override
    public void accept(GroupBy groupBy) {
        this.groupBy = groupBy;
    }

    @Override
    public void accept(Where where) {
        this.where = where;
    }
}
