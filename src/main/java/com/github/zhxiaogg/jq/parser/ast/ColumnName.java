package com.github.zhxiaogg.jq.parser.ast;

import lombok.Data;

@Data
public class ColumnName implements AstNode, AstBuilder<ColumnName> {
    private final String name;

    @Override
    public ColumnName build() {
        return this;
    }


    public interface AcceptColumnName {
        void accept(ColumnName columnName);
    }
}
