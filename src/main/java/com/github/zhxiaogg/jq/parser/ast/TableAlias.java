package com.github.zhxiaogg.jq.parser.ast;

import lombok.Data;

@Data
public class TableAlias implements AstNode, AstBuilder<TableAlias> {
    private final String name;

    @Override
    public TableAlias build() {
        return this;
    }

    public interface AcceptTableAlias {
        void accept(TableAlias tableAlias);
    }
}
