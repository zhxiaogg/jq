package com.github.zhxiaogg.jq.parser.ast;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class TableName implements AstNode,AstBuilder<TableName> {
    private final String name;

    @Override
    public TableName build() {
        return this;
    }

    public interface AcceptTableName {
        void accept(TableName tableName);
    }
}
