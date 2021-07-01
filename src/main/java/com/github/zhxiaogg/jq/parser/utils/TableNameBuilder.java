package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.TableName;
import lombok.Data;

@Data
class TableNameBuilder implements AstBuilder<TableName> {
    private final String name;

    @Override
    public TableName build() {
        return new TableName(name);
    }

}
