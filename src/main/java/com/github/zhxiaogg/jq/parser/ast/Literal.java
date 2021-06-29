package com.github.zhxiaogg.jq.parser.ast;

import lombok.Data;

@Data
public class Literal implements AstNode, AstBuilder<Literal> {
    private final Object value;

    public interface AcceptLiteral {
        void accept(Literal literal);
    }

    @Override
    public Literal build() {
        return this;
    }
}
