package com.github.zhxiaogg.jq.parser.utils;

import com.github.zhxiaogg.jq.ast.TableAlias;
import lombok.Data;

@Data
class TableAliasBuilder implements AstBuilder<TableAlias> {
    private final String name;

    @Override
    public TableAlias build() {
        return new TableAlias(name);
    }

}
