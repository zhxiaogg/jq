package com.github.zhxiaogg.jq.parser.ast;

import lombok.Data;

@Data
public class FuncName implements AstNode, AstBuilder<FuncName> {
    private final String name;

    public interface AcceptFuncName {
        void accept(FuncName funcName);
    }

    @Override
    public FuncName build() {
        return this;
    }
}
