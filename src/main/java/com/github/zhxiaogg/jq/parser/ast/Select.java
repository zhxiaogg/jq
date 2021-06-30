package com.github.zhxiaogg.jq.parser.ast;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
public class Select implements AstNode {
    private final List<ResultColumn> resultColumns;
    private final FromTable fromTable;
    private final Where where;

    public interface AcceptSelect {
        void accept(Select select);
    }

    public static class SelectBuilder implements AstBuilder<Select>, ResultColumn.AcceptResultColumn, FromTable.AcceptFromTable, Where.AcceptWhere {
        private final List<ResultColumn> resultColumns = new ArrayList<>();
        private FromTable fromTable;
        private Where where;

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
            return new Select(resultColumns, fromTable, where);
        }

        @Override
        public void accept(Where where) {
            this.where = where;
        }
    }
}
