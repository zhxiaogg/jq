package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.ColumnName;
import lombok.Data;

@Data
class ColumnNameBuilder implements AstBuilder<ColumnName> {
    private final String name;

    @Override
    public ColumnName build() {
        return new ColumnName(name);
    }
}
