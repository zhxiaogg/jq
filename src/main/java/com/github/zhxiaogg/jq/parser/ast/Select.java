package com.github.zhxiaogg.jq.parser.ast;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
public class Select implements AstNode {
    private final List<ResultColumn> resultColumns;
    private final FromTable fromTable;

    public Select(List<ResultColumn> resultColumns, FromTable fromTable) {
        this.resultColumns = resultColumns;
        this.fromTable = fromTable;
    }

    public interface AcceptSelect {
        void accept(Select select);
    }

    public static class SelectBuilder implements AstBuilder<Select>, ResultColumn.AcceptResultColumn, FromTable.AcceptFromTable {
        private final List<ResultColumn> resultColumns = new ArrayList<>();
        private FromTable fromTable;

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
            return new Select(resultColumns, fromTable);
        }
    }
}
